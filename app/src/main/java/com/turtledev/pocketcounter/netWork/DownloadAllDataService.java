package com.turtledev.pocketcounter.netWork;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.turtledev.pocketcounter.mDataBase.DBAdapterRx;
import com.turtledev.pocketcounter.models.ChargesResponse;
import com.turtledev.pocketcounter.models.DetailChargesResponse;
import com.turtledev.pocketcounter.netWork.commands.ChargesAction;
import com.turtledev.pocketcounter.netWork.commands.DetailChargesAction;

/**
 * Created by PavloByinyk.
 * Service for download all data from server
 */
public class DownloadAllDataService extends IntentService {

    private static final String TAG = DownloadAllDataService.class.getName();
    public static final String USER_TOKEN = "userToken";
    private static final int MAX_RETRIES = 2;
    private int result = Activity.RESULT_CANCELED;
    public static final String NOTIFICATION = "com.turtledev.pockercounter.netWork.DownloadAllDataService";
    public static final String RESULT = "result";

    public DownloadAllDataService() {
        super("DownloadAllDataService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String token = intent.getStringExtra(USER_TOKEN);
                getCharges(token);
        }
    }
    private void getCharges(final String userToken) {
        ChargesAction action = new ChargesAction(this,userToken, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                ChargesResponse chargesDict = (ChargesResponse) response;
                DBAdapterRx.getDBAdapterRx(getApplicationContext()).insertServerDataChargesToDB(chargesDict.getData());
                getDetailCharges(userToken);

            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG , error.toString());
            }
        }, MAX_RETRIES);
        action.run();


    }
    private void getDetailCharges(String userToken) {
        DetailChargesAction action = new DetailChargesAction(this,userToken, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                DetailChargesResponse detailsCharges = (DetailChargesResponse) response;
                DBAdapterRx.getDBAdapterRx(getApplicationContext()).insertServerDataDetailsToDB(detailsCharges.getData());
                result = Activity.RESULT_OK;
                publishResults(result);
                stopSelf();
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG , error.toString());
}
        }, MAX_RETRIES);
        action.run();
    }
    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
