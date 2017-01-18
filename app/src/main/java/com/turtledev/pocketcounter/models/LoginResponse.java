package com.turtledev.pocketcounter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PavloByinyk.
 */
public class LoginResponse {

    @SerializedName(value = "name")
    String name;

    @SerializedName(value = "user-token")
    String userToken;

    @SerializedName(value = "email")
    String email;

    @SerializedName(value = "ownerId")
    String ownerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getEmail() {
        return email;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "name='" + name + '\'' +
                ", userToken='" + userToken + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
