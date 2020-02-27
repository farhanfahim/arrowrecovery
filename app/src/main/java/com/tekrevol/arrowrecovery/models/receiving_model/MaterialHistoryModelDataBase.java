package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MaterialHistoryModelDataBase {


    public MaterialHistoryModelDataBase(String updated_at, String created_at, Double rhodium_price, Double palladium_price, Double platinum_price, String currency, Date date, int id, long uId) {
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.rhodium_price = rhodium_price;
        this.palladium_price = palladium_price;
        this.platinum_price = platinum_price;
        this.currency = currency;
        this.date = date;
        this.id = id;
        this.uId = uId;
    }

    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("rhodium_price")
    private Double rhodium_price;
    @Expose
    @SerializedName("palladium_price")
    private Double palladium_price;
    @Expose
    @SerializedName("platinum_price")
    private Double platinum_price;
    @Expose
    @SerializedName("currency")
    private String currency;
    @Expose
    @SerializedName("date")
    private Date date;
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

    public Double getRhodium_price() {
        return rhodium_price;
    }

    public void setRhodium_price(Double rhodium_price) {
        this.rhodium_price = rhodium_price;
    }

    public Double getPalladium_price() {
        return palladium_price;
    }

    public void setPalladium_price(Double palladium_price) {
        this.palladium_price = palladium_price;
    }

    public Double getPlatinum_price() {
        return platinum_price;
    }

    public void setPlatinum_price(Double platinum_price) {
        this.platinum_price = platinum_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
