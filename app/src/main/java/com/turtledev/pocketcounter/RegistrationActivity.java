package com.turtledev.pocketcounter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.models.UserLogin;
import com.turtledev.pocketcounter.netWork.NetworkCallback;
import com.turtledev.pocketcounter.netWork.commands.RegistrationAction;

/**
 * Created by me.
 */

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getName();
    private static final int MAX_RETRIES = 2;
    private EditText password,email,login;
    private Button registration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        findViews();
        onRegistrationBtnClick();
    }
    // check registration field , if all ok call registrateNewUser()
    private void onRegistrationBtnClick(){
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.length()==0 || email.length()==0 || login.length()==0){
                    Toast.makeText(RegistrationActivity.this, R.string.log_toast,Toast.LENGTH_SHORT).show();
                }else

                    registrateNewUser(login.getText().toString(),password.getText().toString(),email.getText().toString());
            }
        });
    }
    private void findViews(){
        password=(EditText) findViewById(R.id.etPasswordRegistr);
        email=(EditText) findViewById(R.id.etEmailRegistr);
        login=(EditText) findViewById(R.id.etLoginRegistr);
        registration=(Button) findViewById(R.id.btn_registration);
    }
    // send info about new user on server
    private void registrateNewUser(String login,String password,String email) {
        RegistrationAction action = new RegistrationAction(new UserLogin(login, password, email),
                new NetworkCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Toast.makeText(RegistrationActivity.this, R.string.registration_ok,Toast.LENGTH_SHORT).show();
                        finish();
                  }
                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(RegistrationActivity.this, R.string.registration_fail,Toast.LENGTH_SHORT).show();
                    }
                }, MAX_RETRIES);

        action.run();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyHelper.closeKeyboard(this);
    }
}
