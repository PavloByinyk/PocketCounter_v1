package com.turtledev.pocketcounter;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.turtledev.pocketcounter.mHelpers.MyBroadcastReceiver;
import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.netWork.DownloadAllDataService;

/**
 * Created by me.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private MyBroadcastReceiver myBroadcastReceiver;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);
        myBroadcastReceiver = new MyBroadcastReceiver();
        // Check if there is saved user data . If there is - start DownloadAllDataService
        // else start LoginActivity
        if (MyHelper.getLoginResponseData(this)!=null)
            startService( new Intent(this, DownloadAllDataService.class).putExtra( DownloadAllDataService.USER_TOKEN, MyHelper.getLoginResponseData(this).getUserToken()));
        else
            startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
    }
    // register myReceiver in this Activity
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcastReceiver, new IntentFilter(
                DownloadAllDataService.NOTIFICATION));
            }
    // unregister myReceiver
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}