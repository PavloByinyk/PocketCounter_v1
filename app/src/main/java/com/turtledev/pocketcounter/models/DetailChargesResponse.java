package com.turtledev.pocketcounter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by PavloByinyk.
 */
public class DetailChargesResponse implements Parcelable{

    private String nextPage;
    private List<DetailCharges> data;

    public DetailChargesResponse(String nextPage, List<DetailCharges> data) {
        this.nextPage = nextPage;
        this.data = data;
    }

    protected DetailChargesResponse(Parcel in) {
        nextPage = in.readString();
        data = in.createTypedArrayList(DetailCharges.CREATOR);
    }

    public static final Creator<DetailChargesResponse> CREATOR = new Creator<DetailChargesResponse>() {
        @Override
        public DetailChargesResponse createFromParcel(Parcel in) {
            return new DetailChargesResponse(in);
        }

        @Override
        public DetailChargesResponse[] newArray(int size) {
            return new DetailChargesResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nextPage);
        dest.writeTypedList(data);
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<DetailCharges> getData() {
        return data;
    }

    public void setData(List<DetailCharges> data) {
        this.data = data;
    }

    public static Creator<DetailChargesResponse> getCREATOR() {
        return CREATOR;
    }
}
