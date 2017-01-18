package com.turtledev.pocketcounter.mInterfaces;

/**
 * Created by PavloByinyk.
 */

public interface Callbacks {
    // update fragments UI after manipulation with data
    void updateUiInFragment();
    // open new fragment with all charges of one type after click on item
    void onChargesSelected(String name);
}
