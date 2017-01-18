package com.turtledev.pocketcounter.netWork.commands;

import android.content.Context;
import android.util.Log;

import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.netWork.NetworkApi;
import com.turtledev.pocketcounter.netWork.NetworkCallback;

/**
 * Created by PavloByinyk.
 */

public class DeleteDetailChargesAction {
    private static final String TAG = DeleteDetailChargesAction.class.getSimpleName();
    private final NetworkApi mNetworkApi;
    private Context context;
    private String userToken;
    private DetailCharges detailCharges;
    private NetworkCallback mNetworkCallback;
    int mRetries;

    public DeleteDetailChargesAction(DetailCharges detailCharges, Context context,  NetworkCallback networkCallback, int retries) {
        this.detailCharges=detailCharges;
        this.context=context;
        mRetries = retries;
        mNetworkCallback = networkCallback;
        mNetworkApi = NetworkApi.getInstance();
    }

    public void run(){
        mNetworkApi.deleteDetailCharges(detailCharges, context, new NetworkCallback() {
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
