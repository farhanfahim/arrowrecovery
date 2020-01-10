package com.tekrevol.arrowrecovery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachments {

    @Expose
    @SerializedName("attachment_url")
    private String attachment_url;
    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("instance_id")
    private int instance_id;
    @Expose
    @SerializedName("instance_type")
    private String instance_type;
    @Expose
    @SerializedName("path")
    private String path;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private int id;

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

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

    public int getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(int instance_id) {
        this.instance_id = instance_id;
    }

    public String getInstance_type() {
        return instance_type;
    }

    public void setInstance_type(String instance_type) {
        this.instance_type = instance_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
