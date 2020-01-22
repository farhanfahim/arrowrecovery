package com.tekrevol.arrowrecovery.models;

public class SearchHistoryModel {

    private String query;

    public SearchHistoryModel() {
    }

    public SearchHistoryModel(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
