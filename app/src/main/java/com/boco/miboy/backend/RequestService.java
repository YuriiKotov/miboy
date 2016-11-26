package com.boco.miboy.backend;

import com.boco.miboy.model.PhotoRequest;
import com.boco.miboy.model.Questionnaire;
import com.boco.miboy.model.Registration;
import com.boco.miboy.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RequestService {

    @Headers("Content-Type: application/json")
    @POST(Urls.registration)
    Call<ServerResponse> registration(@Body Registration registration);

    @Headers("Content-Type: application/json")
    @POST(Urls.questionnaire)
    Call<ServerResponse> questionnaire(@Body Questionnaire questionnaire);

    @Headers("Content-Type: application/json")
    @POST(Urls.recipes)
    Call<ServerResponse> post(@Body PhotoRequest message);
}
