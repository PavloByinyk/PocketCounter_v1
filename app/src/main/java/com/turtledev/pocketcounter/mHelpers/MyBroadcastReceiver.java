package com.turtledev.pocketcounter.mHelpers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.turtledev.pocketcounter.MainActivity;
import com.turtledev.pocketcounter.R;
import com.turtledev.pocketcounter.netWork.DownloadAllDataService;

import static android.app.Activity.RESULT_OK;

/**
 * Created by PavloByinyk.
 *  BroadcastReceiver for notify activities when service ends load data
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = MyBroadcastReceiver.class.getName();
    //call when service ends download data, close old activity - start new one
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int resultCode = bundle.getInt(DownloadAllDataService.RESULT);
            if (resultCode == RESULT_OK) {
                context.startActivity(new Intent(context, MainActivity.class));
                ((Activity) context).finish();
            } else {
                Toast.makeText(context, R.string.some_problems_on_server,Toast.LENGTH_LONG).show();
            }
        }
    }
}

