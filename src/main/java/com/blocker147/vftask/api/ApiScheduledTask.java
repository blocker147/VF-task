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
    private static final String API_KEY = "e84e7e35d156d408c37d836510c23946";

    @Autowired
    private ApiService apiService;

    public void callApi() {
        int limit = random.nextInt(3) + 1;//1-3
        log.info("Call scheduled task with parameter limit=" + limit);

        Map<String, String> params = new LinkedHashMap<>();
        params.put("access_key", API_KEY);
        params.put("limit", String.valueOf(limit));

        Map<String, Object> response = apiService.callApi(params);
        log.info(response.get("apiResponse").toString());
    }
}
