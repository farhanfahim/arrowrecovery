package com.tekrevol.arrowrecovery.models.receiving_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;
import com.tekrevol.arrowrecovery.models.Attachments;

import java.util.List;

public class Product {
    @Expose
    @SerializedName("products")
    private List<ProductDetailModel> products;

    @Expose
    @SerializedName("total_pages")
    private int total_pages;

    public List<ProductDetailModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailModel> products) {
        this.products = products;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }



    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
