package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

import java.util.List;

public class OrderModel {

    @Expose
    @SerializedName("product_count")
    private int product_count;
    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("status")
    private int status;
    @Expose
    @SerializedName("amount")
    private int amount;
    @Expose
    @SerializedName("rhodium_price")
    private double rhodium_price;
    @Expose
    @SerializedName("palladium_price")
    private double palladium_price;
    @Expose
    @SerializedName("platinum_price")
    private double platinum_price;
    @Expose
    @SerializedName("delivery_date_time")
    private String delivery_date_time;
    @Expose
    @SerializedName("delivery_mode")
    private int delivery_mode;
    @Expose
    @SerializedName("collection_center_id")
    private String collection_center_id;
    @Expose
    @SerializedName("user_id")
    private int user_id;
    @Expose
    @SerializedName("id")
    private int id;

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    @Expose
    @SerializedName("invoice_url")
    private String invoiceUrl;


    @Expose
    @SerializedName("estimated_amount")
    private int estimatedAmount;


    @Expose
    @SerializedName("order_products")
    private List<OrderProductModel> orderProductModels;

    @Expose
    @SerializedName("manufacturerModel")
    private ManufacturerModel manufacturerModel;

    @Expose
    @SerializedName("user")
    private UserModel userModel;


    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<OrderProductModel> getOrderProductModels() {
        return orderProductModels;
    }

    public void setOrderProductModels(List<OrderProductModel> orderProductModels) {
        this.orderProductModels = orderProductModels;
    }


    public ManufacturerModel getManufacturerModel() {
        return manufacturerModel;
    }

    public void setManufacturerModel(ManufacturerModel manufacturerModel) {
        this.manufacturerModel = manufacturerModel;
    }


    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getRhodium_price() {
        return rhodium_price;
    }

    public void setRhodium_price(double rhodium_price) {
        this.rhodium_price = rhodium_price;
    }

    public double getPalladium_price() {
        return palladium_price;
    }

    public void setPalladium_price(int palladium_price) {
        this.palladium_price = palladium_price;
    }

    public double getPlatinum_price() {
        return platinum_price;
    }

    public void setPlatinum_price(int platinum_price) {
        this.platinum_price = platinum_price;
    }

    public String getDelivery_date_time() {
        return delivery_date_time;
    }

    public void setDelivery_date_time(String delivery_date_time) {
        this.delivery_date_time = delivery_date_time;
    }

    public int getDelivery_mode() {
        return delivery_mode;
    }

    public void setDelivery_mode(int delivery_mode) {
        this.delivery_mode = delivery_mode;
    }

    public String getCollection_center_id() {
        return collection_center_id;
    }

    public void setCollection_center_id(String collection_center_id) {
        this.collection_center_id = collection_center_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(int estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
