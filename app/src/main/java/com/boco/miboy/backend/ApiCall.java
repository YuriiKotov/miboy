package com.boco.miboy.backend;

import android.util.Log;

import com.boco.miboy.enums.QueryEvent;
import com.boco.miboy.model.Questionnaire;
import com.boco.miboy.model.Registration;
import com.boco.miboy.enums.AuthEvent;

import org.greenrobot.eventbus.EventBus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    private static final String TAG = ApiCall.class.getSimpleName();

    public void registration(String userUid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestService service = retrofit.create(RequestService.class);

        Call<ResponseBody> call = service.registration(new Registration(userUid));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: onPost: ");
                EventBus.getDefault().post(AuthEvent.SUCCESS);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EventBus.getDefault().post(AuthEvent.FAILURE);
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }

    public void questionnaire(Questionnaire questionnaire) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestService service = retrofit.create(RequestService.class);

        Call<ResponseBody> call = service.questionnaire(questionnaire);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: onPost: ");
                EventBus.getDefault().post(QueryEvent.SUCCESS);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EventBus.getDefault().post(QueryEvent.FAILURE);
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }
}