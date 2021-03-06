package com.blocker147.vftask.api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ApiService {
    private static final Logger log = LoggerFactory.getLogger(ApiService.class);
    private static final String path = "http://api.aviationstack.com/v1/countries";

    public String setParametersToURL(Map<String, String> parameters) {
        log.info("setParametersToURL()");
        StringBuilder pathWithParameters = new StringBuilder(path);
        pathWithParameters.append("?");
        for (String param : parameters.keySet()) {
            pathWithParameters.append(param).append("=").append(parameters.get(param)).append("&");
        }
        return pathWithParameters.toString();
    }

    public ApiResponse getDataFromJson(String json) {
        log.info("getDataFromJson()");
        return new Gson().fromJson(json, ApiResponse.class);
    }

    @Cacheable(value = "apiResponse", key = "#parameters.get('limit')")
    public Map<String, Object> callApi(Map<String, String> parameters) {
        log.info("callApi()");
        Map<String, Object> response = new LinkedHashMap<>();
        ApiResponse apiResponse = null;
        try {
            /*set parameters*/
            String pathWithParameters = setParametersToURL(parameters);

            /*open connection*/
            URL url = new URL(pathWithParameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            /*get response content*/
            int status = connection.getResponseCode();
            response.put("status", status);

            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                /*get data from json*/
                apiResponse = getDataFromJson(content.toString());
                in.close();

            } else {
                log.error("Unable to get data from response. Status code " + HttpStatus.valueOf(status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.put("apiResponse", apiResponse);
        return response;
    }
}
