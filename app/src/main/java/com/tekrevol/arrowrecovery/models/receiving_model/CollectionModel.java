package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

import java.util.List;

public class CollectionModel {

    @Expose
    @SerializedName("working_days")
    private List<Working_daysEntity> workingDays;
    @Expose
    @SerializedName("distance")
    private double distance;
    @Expose
    @SerializedName("deleted_at")
    private String deletedAt;
    @Expose
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("slot")
    private int slot;
    @Expose
    @SerializedName("closing_time")
    private String closingTime;
    @Expose
    @SerializedName("opening_time")
    private String openingTime;
    @Expose
    @SerializedName("longitude")
    private String longitude;
    @Expose
    @SerializedName("latitude")
    private String latitude;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private int id;

    public List<Working_daysEntity> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<Working_daysEntity> workingDays) {
        this.workingDays = workingDays;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class WorkingDaysEntity {
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
