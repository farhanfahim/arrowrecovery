package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.models.UserDetails;

public class VehicleModels {

    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("make_id")
    private int make_id;
    @Expose
    @SerializedName("id")
    private int id;

    public VehicleModels getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModels vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    @Expose
    @SerializedName("details")
    private VehicleModels vehicleModel;



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMake_id() {
        return make_id;
    }

    public void setMake_id(int make_id) {
        this.make_id = make_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
