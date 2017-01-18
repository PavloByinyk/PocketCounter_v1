package com.turtledev.pocketcounter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PavloByinyk.
 */
public class Charges implements Parcelable {
    private String name;
    @SerializedName(value = "objectId")
    private String objectId;
    @SerializedName(value = "userId")
    private String userId;
    public Charges(String newCharge) {
        this.name=newCharge;
    }
    public Charges(String newCharge,String userId) {
        this.name=newCharge;
        this.userId=userId;
    }

    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(this.name);
        dest.writeString(this.objectId);
    }

    public Charges() {
    }

    public Charges(Parcel in) {
        this.name = in.readString();
        this.objectId=in.readString();
    }

    public static final Creator<Charges> CREATOR = new Creator<Charges>() {
        @Override
        public Charges createFromParcel(Parcel source) {
            return new Charges(source);
        }

        @Override
        public Charges[] newArray(int size) {
            return new Charges[size];
        }
    };
}
