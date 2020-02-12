package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushNotificationModel {

    @Expose
    @SerializedName("click_action")
    private String clickAction;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("server_time")
    private ServerTime serverTime;
    @Expose
    @SerializedName("ref_id")
    private int refId;
    @Expose
    @SerializedName("action_type")
    private int actionType;

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServerTime getServerTime() {
        return serverTime;
    }

    public void setServerTime(ServerTime serverTime) {
        this.serverTime = serverTime;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public static class ServerTime {
        @Expose
        @SerializedName("timezone_type")
        private int timezoneType;
        @Expose
        @SerializedName("timezone")
        private String timezone;
        @Expose
        @SerializedName("date")
        private String date;

        public int getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(int timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
