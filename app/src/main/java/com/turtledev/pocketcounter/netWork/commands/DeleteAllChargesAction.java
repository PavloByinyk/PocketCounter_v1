package com.turtledev.pocketcounter.netWork.commands;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.netWork.NetworkApi;
import com.turtledev.pocketcounter.netWork.NetworkCallback;

/**
 * Created by PavloByinyk.
 */
public class DeleteAllChargesAction {
    private static final String TAG = DeleteAllChargesAction.class.getSimpleName();
    private final NetworkApi mNetworkApi;
    private Context context;
    private String userToken;
    private Charges charges;
    private NetworkCallback mNetworkCallback;
    int mRetries;

    public DeleteAllChargesAction(Charges charges, Context context,  NetworkCallback networkCallback, int retries) {
        this.charges=charges;
        this.context=context;
        mRetries = retries;
        mNetworkCallback = networkCallback;
        mNetworkApi = NetworkApi.getInstance();
    }

    public void run(){
        mNetworkApi.deleteAllCharges(charges, context, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                mNetworkCallback.onSuccess(response);
                mNetworkApi.deleteAllDetailCharges(charges, context, new NetworkCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mNetworkCallback.onSuccess(response);
                        Toast.makeText(context, "Success",Toast.LENGTH_SHORT).show();
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
