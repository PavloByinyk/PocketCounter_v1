package com.turtledev.pocketcounter.netWork.commands;

import android.content.Context;
import android.util.Log;

import com.turtledev.pocketcounter.netWork.NetworkApi;
import com.turtledev.pocketcounter.netWork.NetworkCallback;

/**
 * Created by PavloByinyk.
 */
public class DetailChargesAction {
    private static final String TAG = DetailChargesAction.class.getSimpleName();
    private final NetworkApi mNetworkApi;
    private String userToken;
    private NetworkCallback mNetworkCallback;
    int mRetries;
    Context context;

    public DetailChargesAction(Context context , String userToken, NetworkCallback networkCallback, int retries) {
        this.userToken=userToken;
        this.context=context;
        mRetries = retries;
        mNetworkCallback = networkCallback;
        mNetworkApi = NetworkApi.getInstance();
    }

    public void run(){
        mNetworkApi.getDetailCharges(context,userToken, new NetworkCallback() {
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
