package com.blocker147.vftask.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class WireMockTest {
    private static final String url = "http://localhost:8080";

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
    void findAnimalByIdShouldReturnException() throws IOException {
        long id = -10L;
        String path = "/animals/" + id;
        int expectedStatusCode = 404;
        String expectedBody = String.format("Animal with id '%d' not found.", id);

        stubFor(get(urlEqualTo(path))
                .willReturn(aResponse()
                        .withStatus(expectedStatusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedBody)));

        HttpResponse httpResponse = makeGetRequestBy(path);
        String actualBody = convertHttpResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(path)));
        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertTrue(actualBody.contains(expectedBody));
    }

    @Test
    void saveAnimal() throws IOException {
        String path = "/animals";
        int expectedStatusCode = 200;
        stubFor(post(urlEqualTo(path))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"name\": \"Name1\""))
                .withRequestBody(containing("\"age\": 1"))
                .withRequestBody(containing("\"weight\": 2"))
                .willReturn(aResponse().withStatus(expectedStatusCode)));

        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("wiremock/saveAnimal.json");
        String jsonString = convertInputStreamToString(jsonInputStream);
        StringEntity entity = new StringEntity(jsonString);
        HttpResponse httpResponse = makePostRequestBy(path, entity);

        verify(postRequestedFor(urlEqualTo(path)).withHeader("Content-Type", equalTo("application/json")));
        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
    }

    private HttpResponse makeGetRequestBy(String path) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url + path);
        return httpClient.execute(request);
    }

    private HttpResponse makePostRequestBy(String path, StringEntity entity) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url + path);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);
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
