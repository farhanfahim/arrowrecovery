package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class DataModel {
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("ref_id")
    private int refId;
    @Expose
    @SerializedName("action_type")
    private String actionType;
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("sender_id")
    private int senderId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
