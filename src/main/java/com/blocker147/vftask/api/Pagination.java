package com.blocker147.vftask.api;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagination that = (Pagination) o;
        return limit == that.limit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit);
    }
}
