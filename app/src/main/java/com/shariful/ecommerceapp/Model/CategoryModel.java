package com.shariful.ecommerceapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {

    @Expose
    @SerializedName("cat_name")
    private String cat_name;

    @Expose
    @SerializedName("total_product")
    private String price;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("success")
    private boolean success;

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
