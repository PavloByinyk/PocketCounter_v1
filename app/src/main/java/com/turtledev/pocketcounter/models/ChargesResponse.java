package com.turtledev.pocketcounter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by PavloByinyk.
 */
public class ChargesResponse implements Parcelable {

    private String nextPage;
    private List<Charges> data;

    public ChargesResponse(String nextPage, List<Charges> data) {
        this.nextPage = nextPage;
        this.data = data;
    }
    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<Charges> getData() {
        return data;
    }

    public void setData(List<Charges> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nextPage);
        dest.writeTypedList(data);
    }

    protected ChargesResponse(Parcel in) {
        this.nextPage = in.readString();
        this.data = in.createTypedArrayList(Charges.CREATOR);
    }

    public static final Creator<ChargesResponse> CREATOR = new Creator<ChargesResponse>() {
        @Override
        public ChargesResponse createFromParcel(Parcel source) {
            return new ChargesResponse(source);
        }

        @Override
        public ChargesResponse[] newArray(int size) {
            return new ChargesResponse[size];
        }
    };
}
