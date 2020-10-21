package com.blocker147.vftask.api;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse that = (ApiResponse) o;
        return Objects.equals(pagination, that.pagination) &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(pagination);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
