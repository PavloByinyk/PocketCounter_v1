package com.turtledev.pocketcounter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.turtledev.pocketcounter.mHelpers.MyBroadcastReceiver;
import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.models.LoginResponse;
import com.turtledev.pocketcounter.models.UserLogin;
import com.turtledev.pocketcounter.netWork.DownloadAllDataService;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
import com.turtledev.pocketcounter.netWork.commands.LoginAction;



/**
 * Created by PavloByinyk.
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private Button logBtn;
    private EditText etLog,etPas;
    private TextView etRegister;
    private Context context;
    private static final int MAX_RETRIES = 2;
    private MyBroadcastReceiver myBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        context=this;
        myBroadcastReceiver = new MyBroadcastReceiver();
        findViews();
        onLogBtnClick();
        onRegisterBtnClick();
   }
    // start registration activity
    private void onRegisterBtnClick(){
        etRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }
    // check login fields, if ok - start MainActivity, else - show toast
    private void onLogBtnClick(){
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etLog.length()==0 || etPas.length()==0){
                    Toast.makeText(LoginActivity.this, R.string.log_toast,Toast.LENGTH_SHORT).show();
                }else {

                    retrofitLoginRequest(etLog.getText().toString(), etPas.getText().toString());
                }
            }
        });
    }
    // find Views
    private void findViews(){
        etLog=(EditText)findViewById(R.id.etLog);
        etPas=(EditText)findViewById(R.id.etPas);
        etRegister=(TextView)findViewById(R.id.tv_sign_up);
        logBtn=(Button)findViewById(R.id.logBtn);
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
        MyHelper.closeKeyboard(this);
    }
    // show Exit dialog when back btn click
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                    }
                }).create().show();



    }
    // send login info on server , check it there and return LoginResponse
    // if all ok, start DownloadAllDataService
    private void retrofitLoginRequest(String login,String password) {
        LoginAction action = new LoginAction(new UserLogin(login, password),
                new NetworkCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        LoginResponse loginResponse = (LoginResponse) response;
                        MyHelper.saveLoginResponseData(context,loginResponse);
                        startService( new Intent(context, DownloadAllDataService.class).putExtra( DownloadAllDataService.USER_TOKEN, loginResponse.getUserToken()));
                    }
                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(LoginActivity.this, R.string.login_fail,Toast.LENGTH_SHORT).show();
                    }
                }, MAX_RETRIES);
        action.run();
    }
}



