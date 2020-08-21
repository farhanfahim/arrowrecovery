package com.tekrevol.arrowrecovery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;
import com.tekrevol.arrowrecovery.searchdialog.core.Searchable;

public class Country {

    @Expose
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("name")
    private String name;
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

  /*  @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
*/
    @Override
    public String toString() {
        return name;
    }
}
