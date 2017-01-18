package com.turtledev.pocketcounter.netWork.commands;

import android.util.Log;

import com.turtledev.pocketcounter.models.UserLogin;
import com.turtledev.pocketcounter.netWork.NetworkApi;
import com.turtledev.pocketcounter.netWork.NetworkCallback;

/**
 * Created by PavloByinyk.
 */
public class RegistrationAction {
    private static final String TAG = RegistrationAction.class.getSimpleName();
    private final NetworkApi mNetworkApi;
    private UserLogin mUserLogin;
    private NetworkCallback mNetworkCallback;
    int mRetries;

    public RegistrationAction(UserLogin userLogin, NetworkCallback networkCallback, int retries) {
        mUserLogin = userLogin;
        mRetries = retries;
        mNetworkCallback = networkCallback;
        mNetworkApi = NetworkApi.getInstance();
    }
    public void run(){
        mNetworkApi.registration(mUserLogin, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                mNetworkCallback.onSuccess(response);
            }
            @Override
            public void onFailure(String error) {
                if (mRetries > 0){
                    Log.v(TAG, "Login retry " + mRetries);
                    mRetries--;
                    run();
                } else {
                    mNetworkCallback.onFailure(error);
                }
            }
        });
    }
}
