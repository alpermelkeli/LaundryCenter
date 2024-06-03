package com.alpermelkeli.laundrycenter.repository.plugAPI;


import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TurnOnOff {

    private static final String BASE_URL = "https://shelly-93-eu.shelly.cloud/";
    private ApiService apiService;

    public TurnOnOff() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void sendDeviceControl(String id, String authKey, String channel, String turn) {
        Call<ResponseBody> call = apiService.sendDeviceControl(id, authKey, channel, turn);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Log.d("Response", responseString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Error", "Response not successful");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", "Request failed", t);
            }
        });
    }


}

