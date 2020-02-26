package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MaterialHistoryModel {

    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("rhodium_price")
    private int rhodium_price;
    @Expose
    @SerializedName("palladium_price")
    private int palladium_price;
    @Expose
    @SerializedName("platinum_price")
    private int platinum_price;
    @Expose
    @SerializedName("currency")
    private String currency;
    @Expose
    @SerializedName("date")
    private String date;
    @Expose
    @SerializedName("id")
    private int id;

    @Id
    long uId;

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getRhodium_price() {
        return rhodium_price;
    }

    public void setRhodium_price(int rhodium_price) {
        this.rhodium_price = rhodium_price;
    }

    public int getPalladium_price() {
        return palladium_price;
    }

    public void setPalladium_price(int palladium_price) {
        this.palladium_price = palladium_price;
    }

    public int getPlatinum_price() {
        return platinum_price;
    }

    public void setPlatinum_price(int platinum_price) {
        this.platinum_price = platinum_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
