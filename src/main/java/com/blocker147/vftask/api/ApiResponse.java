package com.blocker147.vftask.api;

import java.util.Arrays;

public class ApiResponse {
    private Pagination pagination;
    private Country[] data;

    public ApiResponse(Pagination pagination, Country[] data) {
        this.pagination = pagination;
        this.data = data;
    }

    public ApiResponse() {
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Country[] getData() {
        return data;
    }

    public void setData(Country[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "pagination=" + pagination +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
