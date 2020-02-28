package com.tekrevol.arrowrecovery.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class UpdatePickupModel {


    @Expose
    @SerializedName("time_slot")
    private String timeSlot;
    @Expose
    @SerializedName("delivery_date")
    private String deliveryDate;
    @Expose
    @SerializedName("delivery_mode")
    private int deliveryMode;
    @Expose
    @SerializedName("pickup_address")
    private String address;


    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
