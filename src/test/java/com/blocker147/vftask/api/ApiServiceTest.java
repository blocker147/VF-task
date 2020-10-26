package com.blocker147.vftask.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {
    private static final String host = "http://api.aviationstack.com";
    private static final String API_KEY = "e84e7e35d156d408c37d836510c23946";
    private static final String path = "/v1/countries";
    private static final int limit = 2;
    private static final String pathWithParameters = path + "?access_key=" + API_KEY + "&limit=" + limit;

    static WireMockServer wireMockServer = new WireMockServer();

    @BeforeAll
    static void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Mock
    private ApiService apiService;

    private Map<String, String> parameters() {
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("access_key", API_KEY);
        parameters.put("limit", String.valueOf(limit));
        return parameters;
    }

    private Map<String, String> noParameters() {
        return new LinkedHashMap<>();
    }

    @Test
    void setParametersToURL() {
        Map<String, String> parameters = parameters();

        when(apiService.setParametersToURL(parameters)).thenReturn(
                host + path + "?access_key=" + API_KEY + "&limit=" + limit + "&");
        String result = apiService.setParametersToURL(parameters);

        assertNotNull(result);
        for (String key : parameters.keySet()) {
            assertTrue(result.contains(parameters.get(key)));
            assertTrue(result.contains(key));
        }
    }

    @Test
    void callApi() {
        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("wiremock/apiGetCountries.json");
        String expectedBody = convertInputStreamToString(jsonInputStream);
        ApiResponse expectedApiResponse = new Gson().fromJson(expectedBody, ApiResponse.class);
        int expectedStatusCode = 200;
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("status", expectedStatusCode);
        expectedResponse.put("apiResponse", expectedApiResponse);

        stubFor(get(urlEqualTo(pathWithParameters))
                .willReturn(aResponse()
                        .proxiedFrom(host)
                        .withStatus(expectedStatusCode)
                        .withBody(expectedBody)));

        Map<String, String> parameters = parameters();
        when(apiService.callApi(parameters)).thenReturn(expectedResponse);
        Map<String, Object> actualResponse = apiService.callApi(parameters);

        assertEquals(expectedStatusCode, actualResponse.get("status"));
        assertEquals(expectedApiResponse, actualResponse.get("apiResponse"));
    }

    @Test
    void callApiShouldReturnInternalServerError() {
        int expectedStatusCode = 500;
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("status", expectedStatusCode);
        expectedResponse.put("apiResponse", null);

        stubFor(get(urlEqualTo(pathWithParameters))
                .willReturn(aResponse()
                        .proxiedFrom(host)
                        .withStatus(expectedStatusCode)
                        .withBody("")));

        when(apiService.callApi(parameters())).thenReturn(expectedResponse);
        Map<String, Object> actualResponse = apiService.callApi(parameters());

        assertEquals(expectedStatusCode, actualResponse.get("status"));
        assertNull(actualResponse.get("apiResponse"));
    }

    @Test
    void callApiShouldReturnAccessTokenNotPassed() {
        String expectedBody = "You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]";
        int expectedStatusCode = 401;
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("status", expectedStatusCode);
        expectedResponse.put("apiResponse", null);

        stubFor(get(urlEqualTo(path))//path without access_key parameter
                .willReturn(aResponse()
                        .proxiedFrom(host)
                        .withStatus(expectedStatusCode)
                        .withBody(expectedBody)));

        when(apiService.callApi(noParameters())).thenReturn(expectedResponse);
        Map<String, Object> actualResponse = apiService.callApi(noParameters());

        assertEquals(expectedStatusCode, actualResponse.get("status"));
        assertNull(actualResponse.get("apiResponse"));
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }
}