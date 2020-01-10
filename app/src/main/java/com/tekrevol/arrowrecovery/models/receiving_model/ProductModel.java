package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

import java.util.List;

public class ProductModel {
    @Expose
    @SerializedName("attachments")
    private List<String> attachments;
    @Expose
    @SerializedName("feature_image_url")
    private String featureImageUrl;
    @Expose
    @SerializedName("rhodium")
    private int rhodium;
    @Expose
    @SerializedName("palladium")
    private int palladium;
    @Expose
    @SerializedName("platinum")
    private int platinum;
    @Expose
    @SerializedName("car_variation")
    private String carVariation;
    @Expose
    @SerializedName("year")
    private int year;
    @Expose
    @SerializedName("serial_number")
    private String serialNumber;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("feature_image")
    private String featureImage;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("manufacturer_id")
    private int manufacturerId;
    @Expose
    @SerializedName("model_id")
    private int modelId;
    @Expose
    @SerializedName("user_id")
    private int userId;
    @Expose
    @SerializedName("id")
    private int id;

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getFeatureImageUrl() {
        return featureImageUrl;
    }

    public void setFeatureImageUrl(String featureImageUrl) {
        this.featureImageUrl = featureImageUrl;
    }

    public int getRhodium() {
        return rhodium;
    }

    public void setRhodium(int rhodium) {
        this.rhodium = rhodium;
    }

    public int getPalladium() {
        return palladium;
    }

    public void setPalladium(int palladium) {
        this.palladium = palladium;
    }

    public int getPlatinum() {
        return platinum;
    }

    public void setPlatinum(int platinum) {
        this.platinum = platinum;
    }

    public String getCarVariation() {
        return carVariation;
    }

    public void setCarVariation(String carVariation) {
        this.carVariation = carVariation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
