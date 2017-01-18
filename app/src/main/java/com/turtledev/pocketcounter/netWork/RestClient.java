package com.turtledev.pocketcounter.netWork;


import com.turtledev.pocketcounter.models.Charges;
import com.turtledev.pocketcounter.models.ChargesResponse;
import com.turtledev.pocketcounter.models.DetailCharges;
import com.turtledev.pocketcounter.models.DetailChargesResponse;
import com.turtledev.pocketcounter.models.LoginResponse;
import com.turtledev.pocketcounter.models.UserLogin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by me.
 * Retrofit interface
 */
public interface RestClient {

    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST",
            "Content-Type: application/json"})
    @POST("users/login")
    Call<LoginResponse> login(@Body UserLogin userLogin);


    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST",
            "Content-Type: application/json"})
    @POST("data/Users")
    Call<ResponseBody> registration(@Body UserLogin userLogin);


    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST",
            "Content-Type: application/json"})
    @GET("data/ChargesDict?pageSize=100&")
    Call<ChargesResponse> getCharges(@Header("user-token") String userToken, @Query("where") String userId);


    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST",
            "Content-Type: application/json"})
    @POST("data/ChargesDict")
    Call<Charges> insertCharges(@Body Charges insertCharge);



    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST",
            "Content-Type: application/json"})
    @GET("data/Charges?pageSize=100&")
    Call<DetailChargesResponse> getDetailCharges(@Header("user-token") String userToken , @Query("where") String userId);


    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST",
            "Content-Type: application/json"})
    @POST("data/Charges")
    Call<DetailCharges> insertDetailCharges(@Body DetailCharges detailCharge);


    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST"})
    @DELETE("data/Charges/{id}")
    Call<Void> deleteDetailCharges(@Path("id") String objectId);

    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST"})
    @DELETE("data/bulk/ChargesDict")
    Call<Void> deleteAllCharges( @Query("where") String chargesName);

    @Headers({"application-id: 9FB3F995-7D5A-D629-FF78-C5CE1E9DB000",
            "secret-key: 81BC7440-0B2E-F347-FF87-8ADC4FDE7B00",
            "application-type: REST"})
    @DELETE("data/bulk/Charges")
    Call<Void> deleteAllDetalCharges( @Query("where") String chargesDetalName);



}
