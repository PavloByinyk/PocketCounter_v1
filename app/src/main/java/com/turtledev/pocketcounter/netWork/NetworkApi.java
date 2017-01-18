package com.turtledev.pocketcounter.netWork;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turtledev.pocketcounter.mHelpers.MyHelper;
import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.models.ChargesResponse;
import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.models.DetailChargesResponse;
import com.turtledev.pocketcounter.models.LoginResponse;
import com.turtledev.pocketcounter.models.UserLogin;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PavloByinyk.
 *  Retrofit methods
 */
public class NetworkApi {

    private static NetworkApi instance;
    private final RestClient mRestClient;

    public synchronized static NetworkApi getInstance(){
        if (instance == null){
            instance = new NetworkApi();
        }
        return instance;
    }
    private NetworkApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.backendless.com/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))

                .build();

        mRestClient = retrofit.create(RestClient.class);
}

    public void login(UserLogin userLogin, final NetworkCallback callback){
        Call<LoginResponse> call = mRestClient.login(userLogin);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void registration(UserLogin userLogin, final NetworkCallback callback){
        Call<ResponseBody> call = mRestClient.registration(userLogin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void getChargesDict(Context context,String userToken, final NetworkCallback callback){
        Call<ChargesResponse> call = mRestClient.getCharges(userToken,"userId='"+ MyHelper.getLoginResponseData(context).getOwnerId() +"'");
        call.enqueue(new Callback<ChargesResponse>() {
            @Override
            public void onResponse(Call<ChargesResponse> call, Response<ChargesResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ChargesResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void postCharges(Charges insertCharge, final NetworkCallback callback){
        Call<Charges> call = mRestClient.insertCharges(insertCharge);
        call.enqueue(new Callback<Charges>() {
            @Override
            public void onResponse(Call<Charges> call, Response<Charges> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Charges> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void postDetailCharges(DetailCharges detailCharge, final NetworkCallback callback){
        Call<DetailCharges> call = mRestClient.insertDetailCharges(detailCharge);
        call.enqueue(new Callback<DetailCharges>() {
            @Override
            public void onResponse(Call<DetailCharges> call, Response<DetailCharges> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<DetailCharges> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void getDetailCharges(Context context , String userToken, final NetworkCallback callback){
        Call<DetailChargesResponse> call = mRestClient.getDetailCharges(userToken , "userId='"+ MyHelper.getLoginResponseData(context).getOwnerId() +"'");
        call.enqueue(new Callback<DetailChargesResponse>() {
            @Override
            public void onResponse(Call<DetailChargesResponse> call, Response<DetailChargesResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<DetailChargesResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void deleteDetailCharges(DetailCharges detailCharges, Context context, final NetworkCallback callback){
        Call<Void> call = mRestClient.deleteDetailCharges(detailCharges.getObjectID().toString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void deleteAllCharges(Charges charges, Context context, final NetworkCallback callback){
        Call<Void> call = mRestClient.deleteAllCharges("name='"+ charges.getName() +"'");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void deleteAllDetailCharges(Charges charges, Context context, final NetworkCallback callback){
        Call<Void> call = mRestClient.deleteAllDetalCharges("chargeName='"+ charges.getName() +"'");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

}
