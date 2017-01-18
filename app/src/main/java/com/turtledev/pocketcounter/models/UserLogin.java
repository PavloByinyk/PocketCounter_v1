package com.turtledev.pocketcounter.models;


import com.google.gson.annotations.SerializedName;
/**
 * Created by PavloByinyk.
 */
public class UserLogin {

    @SerializedName(value = "name")
    String name;

    String login;

    @SerializedName(value = "password")
    String password;

    @SerializedName(value = "email")
    String email;

    public UserLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserLogin(String name, String password,String email) {
        this.name = name;
        this.password = password;
        this.email=email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
}
