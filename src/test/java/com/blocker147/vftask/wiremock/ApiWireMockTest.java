package com.blocker147.vftask.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.*;

public class ApiWireMockTest {
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

    @Test
    void apiGetCountries() throws IOException {
        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("wiremock/apiGetCountries.json");
        String expectedBody = convertInputStreamToString(jsonInputStream);

        int expectedStatusCode = 200;
        stubFor(get(urlEqualTo(pathWithParameters))
                .willReturn(aResponse()
                        .proxiedFrom(host)
                        .withStatus(expectedStatusCode)
                        .withBody(expectedBody)));

        HttpResponse httpResponse = makeGetRequestBy();
        String actualBody = convertHttpResponseToString(httpResponse);

        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
        assertNotNull(actualBody);
        assertNotEquals(0, actualBody.length());
    }

    private HttpResponse makeGetRequestBy() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(host + pathWithParameters);
        return httpClient.execute(request);
    }

    private String convertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }
}
