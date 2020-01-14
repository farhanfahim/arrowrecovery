package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;
import com.tekrevol.arrowrecovery.models.Attachments;

import java.util.List;

public class ProductDetailModel {


    @Expose
    @SerializedName("vehicle_model")
    private VehicleModelEntity vehicleModel;
    @Expose
    @SerializedName("attachments")
    private List<Attachments> attachments;
    @Expose
    @SerializedName("feature_image_url")
    private String feature_image_url;
    @Expose
    @SerializedName("is_featured")
    private int isFeatured;
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
    private String car_variation;
    @Expose
    @SerializedName("year")
    private int year;
    @Expose
    @SerializedName("serial_number")
    private String serial_number;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("feature_image")
    private String feature_image;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("manufacturer_id")
    private int manufacturer_id;
    @Expose
    @SerializedName("model_id")
    private int model_id;
    @Expose
    @SerializedName("user_id")
    private int user_id;
    @Expose
    @SerializedName("id")
    private int id;



    public List<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
    }

    public String getFeature_image_url() {
        return feature_image_url;
    }

    public void setFeature_image_url(String feature_image_url) {
        this.feature_image_url = feature_image_url;
    }

    public VehicleModelEntity getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModelEntity vehicleModel) {
        this.vehicleModel = vehicleModel;
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

    public String getCar_variation() {
        return car_variation;
    }

    public void setCar_variation(String car_variation) {
        this.car_variation = car_variation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(int manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
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

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }


    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
