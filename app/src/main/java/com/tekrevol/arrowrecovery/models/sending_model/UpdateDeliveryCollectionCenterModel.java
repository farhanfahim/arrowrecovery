package com.tekrevol.arrowrecovery.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class UpdateDeliveryCollectionCenterModel {


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
    @SerializedName("collection_center_id")
    private int collectionCenterId;

    public int getCollectionCenterId() {
        return collectionCenterId;
    }

    public void setCollectionCenterId(int collectionCenterId) {
        this.collectionCenterId = collectionCenterId;
    }

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

}
