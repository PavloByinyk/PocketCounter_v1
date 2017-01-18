package com.turtledev.pocketcounter.mFragments;

import android.support.v4.app.Fragment;

import com.turtledev.pocketcounter.R;
/**
 * Created by PavloByinyk.
 * abstract class for control all fragments in mainActivity
 */

public abstract class AbstractFragment extends Fragment  {

    public static final int CONT = R.id.container;

    public int GetContFragment() {
        return CONT;
    }
    public String getTagFragment() {
        return null;
    }
    // change fragments
    public void work() {}
    public void updateUI(){}
}
