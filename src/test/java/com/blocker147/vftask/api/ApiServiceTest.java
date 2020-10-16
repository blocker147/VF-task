package com.blocker147.vftask.api;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiServiceTest {

    @Autowired
    private ApiService apiService;

    @Autowired
    private CacheManager cacheManager;

    private static final int limit = 2;

    private Map<String, String> parameters() {
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("limit", String.valueOf(limit));
        return parameters;
    }

    private ApiResponse apiResponse() {
        Country[] data = new Country[limit];
        for (int i = 0; i < limit; i++) {
            data[i] = new Country("Capital" + i, i);
        }
        return new ApiResponse(new Pagination(limit), data);
    }

    @Test
    void setParametersToURL() {
        Map<String, String> parameters = parameters();
        String result = apiService.setParametersToURL(parameters);

        assertNotNull(result);
        for (String key : parameters.keySet()) {
            assertTrue(result.contains(parameters.get(key)));
            assertTrue(result.contains(key));
        }
    }

    @Test
    void getDataFromJson() {
        ApiResponse expected = apiResponse();
        String json = new Gson().toJson(expected, ApiResponse.class);
        ApiResponse actual = apiService.getDataFromJson(json);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void callApi() {
        ApiResponse expected = apiResponse();
        ApiResponse actual = apiService.callApi(parameters());
        System.out.println(actual);

        assertNotNull(actual);
        assertNotNull(cacheManager.getCache("apiResponse"));
        assertEquals(expected.getPagination(), actual.getPagination());
        assertEquals(expected.getData().length, actual.getData().length);
    }
}