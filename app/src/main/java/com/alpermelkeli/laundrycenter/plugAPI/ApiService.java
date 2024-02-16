package com.alpermelkeli.laundrycenter.plugAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

public interface ApiService {
    @FormUrlEncoded
    @POST("device/relay/control")
    Call<ResponseBody> sendDeviceControl(
            @Field("id") String id,
            @Field("auth_key") String authKey,
            @Field("channel") String channel,
            @Field("turn") String turn
    );


}

