package com.boco.miboy.backend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.boco.miboy.activity.MainActivity;
import com.boco.miboy.activity.QueryActivity;
import com.boco.miboy.other.Storage;

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
                EventBus.getDefault().post(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EventBus.getDefault().post(false);
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }
}