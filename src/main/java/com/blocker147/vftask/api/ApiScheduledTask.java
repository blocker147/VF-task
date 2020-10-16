package com.blocker147.vftask.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@Component
public class ApiScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(ApiScheduledTask.class);
    private static final Random random = new Random();

    @Autowired
    private ApiService apiService;

    public void callApi() {
        int limit = random.nextInt(3) + 1;//1-3
        log.info("Call scheduled task with parameter limit=" + limit);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("limit", String.valueOf(limit));
        ApiResponse apiResponse = apiService.callApi(params);
        log.info(apiResponse.toString());
    }
}
