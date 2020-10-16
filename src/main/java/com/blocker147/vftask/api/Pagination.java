package com.blocker147.vftask.api;

public class Pagination {
    private int limit;

    public Pagination() {
    }

    public Pagination(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "limit=" + limit +
                '}';
    }
}
