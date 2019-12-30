package com.tekrevol.arrowrecovery.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class LoginSendingModel {


    @Expose
    @SerializedName("device_token")
    private String deviceToken;
    @Expose
    @SerializedName("device_type")
    private String deviceType;
    @Expose
    @SerializedName("password")
    private String password;
    @Expose
    @SerializedName("email")
    private String email;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
