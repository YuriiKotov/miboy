package com.boco.miboy.backend;

import com.boco.miboy.model.PhotoRequest;
import com.boco.miboy.model.Questionnaire;
import com.boco.miboy.model.Registration;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestService {

    @Headers("Content-Type: application/json")
    @POST(Urls.registration)
    Call<ResponseBody> registration(@Body Registration registration);

    @Headers("Content-Type: application/json")
    @POST(Urls.questionnaire)
    Call<ResponseBody> questionnaire(@Body Questionnaire questionnaire);

    @Headers("Content-Type: application/json")
    @POST(Urls.recipes)
    Call<ResponseBody> post(@Body PhotoRequest message);
}
