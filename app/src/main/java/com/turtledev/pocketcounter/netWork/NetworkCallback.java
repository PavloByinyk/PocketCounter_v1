package com.turtledev.pocketcounter.netWork;

/**
 * Created by PavloByinyk.
 */
public interface NetworkCallback {
    void onSuccess(Object response);
    void onFailure(String error);
}
