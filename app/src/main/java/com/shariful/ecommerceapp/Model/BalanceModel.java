package com.shariful.ecommerceapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceModel {

    @Expose
    @SerializedName("message")    //for response
    private String message;

    @Expose
    @SerializedName("email")  //for saving data
    private String email;

    @Expose
    @SerializedName("points")  //for saving data
    private String points;

    @Expose
    @SerializedName("success") //for boolean response
    private boolean success;

    @Expose
    @SerializedName("text")  //for points retriving
    private String text;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
