package com.tekrevol.arrowrecovery.models;

import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class FilterModel {

    public String text;
    public String filter;


    public FilterModel(String text, String filter) {
        this.text = text;
        this.filter = filter;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
