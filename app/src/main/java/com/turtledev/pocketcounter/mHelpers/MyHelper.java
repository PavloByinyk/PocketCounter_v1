package com.turtledev.pocketcounter.mHelpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.turtledev.pocketcounter.models.LoginResponse;

import static android.content.Context.MODE_PRIVATE;
import static com.turtledev.pocketcounter.mHelpers.MyConstant.USER_DATA;

/**
 * Created by PavloByinyk.
 */
public class MyHelper {
    //save all data about user
    public static void saveLoginResponseData(Context context, LoginResponse userData){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstant.SHARED_PREF,context.getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userData); // myObject - instance of MyObject
        prefsEditor.putString(USER_DATA, json);
        prefsEditor.commit();
    }
    // get all data about user
    public static LoginResponse getLoginResponseData(Context context){
        SharedPreferences sPref = context.getSharedPreferences(MyConstant.SHARED_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(USER_DATA, null);
        return  gson.fromJson(json, LoginResponse.class);
    }
    // delete all data about user
    public static void clearLoginResponseData(Context context){
        SharedPreferences sPref = context.getSharedPreferences(MyConstant.SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sPref.edit();
        prefEditor.remove(USER_DATA);
        prefEditor.commit();
    }
//    public static void closeKeyboard(Context context, View view){
//        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
    public static void closeKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
