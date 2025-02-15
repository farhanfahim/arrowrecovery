package com.tekrevol.arrowrecovery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;
import com.tekrevol.arrowrecovery.searchdialog.core.Searchable;

public class States implements Searchable {

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("short_name")
    private String shortName;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Expose
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String states) {
        this.name = states;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

    @Override
    public String getTitle() {
        return name;
    }
}
