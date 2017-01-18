package com.turtledev.pocketcounter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by PavloByinyk.
 */
public class DetailCharges implements Parcelable{

    @SerializedName(value = "chargeName")
    private String parentChargeNAme;

    @SerializedName(value = "objectId")
    private String objectId;

    @SerializedName(value = "userId")
    private String userId;

    private String description;
    private double sum;

    public void setTime(Date time) {
        this.time = time;
    }

    private Date time;

    public DetailCharges() {
    }

    public DetailCharges(String parentChargeNAme, String description, double sum,String userId) {
        this.parentChargeNAme = parentChargeNAme;
        this.description = description;
        time = new Date();
        this.sum = sum;
        this.userId=userId;
    }
    public DetailCharges(String objectId,String parentChargeNAme, String description, double sum,String userId) {
        this.objectId=objectId;
        this.parentChargeNAme = parentChargeNAme;
        this.description = description;
        time = new Date();
        this.sum = sum;
        this.userId=userId;
    }

    public Date getTime() {
        return time;
    }

    protected DetailCharges(Parcel in) {
        parentChargeNAme = in.readString();
        objectId = in.readString();
        description = in.readString();
        sum = in.readDouble();
        userId=in.readString();
    }

    public static final Creator<DetailCharges> CREATOR = new Creator<DetailCharges>() {
        @Override
        public DetailCharges createFromParcel(Parcel in) {
            return new DetailCharges(in);
        }

        @Override
        public DetailCharges[] newArray(int size) {
            return new DetailCharges[size];
        }
    };

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectID() {
        return objectId;
    }

    public void setObjectID(String objectID) {
        this.objectId = objectID;
    }

    public String getParentChargeNAme() {
        return parentChargeNAme;
    }

    public void setParentChargeNAme(String parentChargeNAme) {
        this.parentChargeNAme = parentChargeNAme;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentChargeNAme);
        dest.writeString(String.valueOf(objectId));
        dest.writeString(description);
        dest.writeString(userId);
        dest.writeDouble(sum);
    }
}
