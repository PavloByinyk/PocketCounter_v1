package com.turtledev.pocketcounter.netWork.commands;

import android.util.Log;

import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.netWork.NetworkApi;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
/**
 * Created by PavloByinyk.
 */
public class InsertChargeAction {
    private static final String TAG = InsertChargeAction.class.getSimpleName();
    private final NetworkApi mNetworkApi;
    private Charges charges;
    private NetworkCallback mNetworkCallback;
    int mRetries;

    public InsertChargeAction( Charges insertCharge, NetworkCallback mNetworkCallback, int mRetries ) {
        this.mNetworkApi = NetworkApi.getInstance();
        this.mRetries = mRetries;
        this.mNetworkCallback = mNetworkCallback;
        this.charges = insertCharge;
    }
    public void run(){
        mNetworkApi.postCharges(charges, new NetworkCallback() {
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
