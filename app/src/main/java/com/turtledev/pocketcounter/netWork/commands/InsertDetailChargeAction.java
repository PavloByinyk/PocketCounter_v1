package com.turtledev.pocketcounter.netWork.commands;

import android.util.Log;

import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.netWork.NetworkApi;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
/**
 * Created by PavloByinyk.
 */
public class InsertDetailChargeAction {
    private static final String TAG = InsertDetailChargeAction.class.getSimpleName();
    private final NetworkApi mNetworkApi;
    private DetailCharges detailCharge;
    private NetworkCallback mNetworkCallback;
    int mRetries;

    public InsertDetailChargeAction( DetailCharges detailCharge, NetworkCallback mNetworkCallback, int mRetries ) {
        this.mNetworkApi = NetworkApi.getInstance();
        this.mRetries = mRetries;
        this.mNetworkCallback = mNetworkCallback;
        this.detailCharge = detailCharge;
    }
    public void run(){
        mNetworkApi.postDetailCharges(detailCharge, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                mNetworkCallback.onSuccess(response);
            }
            @Override
            public void onFailure(String error) {
                if (mRetries > 0){
                    Log.v(TAG, "charges retry " + mRetries);
                    mRetries--;
                    run();
                } else {
                    mNetworkCallback.onFailure(error);
                }
            }
        });
    }
}
