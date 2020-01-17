package com.tekrevol.arrowrecovery.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class UpdateCartModel {

    @Expose
    @SerializedName("quantity")
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
