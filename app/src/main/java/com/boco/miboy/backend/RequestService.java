package com.boco.miboy.backend;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestService {

    @Headers("Content-Type: application/json")
    @POST(Urls.registration)
    Call<ResponseBody> registration(@Body Registration registration);
//
//    @Headers("Content-Type: application/json")
//    @POST(Urls.authorization)
//    Call<ResponseBody> authorize(@Body AuthorizationMessage message);
//
//    @Headers("Content-Type: application/json")
//    @POST(Urls.start)
//    Call<ResponseBody> start(@Body AlexaMessage message);
//
//    @Headers("Content-Type: application/json")
//    @POST(Urls.stop)
//    Call<ResponseBody> stop(@Body AlexaMessage message);
//
    @Headers("Content-Type: application/json")
    @POST(Urls.recipes)
    Call<ServerResponse> post(@Body Message message);
}
