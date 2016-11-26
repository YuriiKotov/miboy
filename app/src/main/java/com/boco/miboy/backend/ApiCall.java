package com.boco.miboy.backend;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.boco.miboy.enums.AuthEvent;
import com.boco.miboy.enums.QueryEvent;
import com.boco.miboy.enums.RecipeEvent;
import com.boco.miboy.model.History;
import com.boco.miboy.model.PhotoRequest;
import com.boco.miboy.model.Questionnaire;
import com.boco.miboy.model.Registration;
import com.boco.miboy.model.ServerResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    private static final String TAG = ApiCall.class.getSimpleName();

    public void registration(String userUid) {
        Retrofit retrofit = getDefaultInstance();
        RequestService service = retrofit.create(RequestService.class);

        Call<ServerResponse> call = service.registration(new Registration(userUid));
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.e(TAG, "onResponse: onPost: " + response.body().toString());
                EventBus.getDefault().post(AuthEvent.SUCCESS);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                EventBus.getDefault().post(AuthEvent.FAILURE);
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }

    public void questionnaire(Questionnaire questionnaire) {
        Retrofit retrofit = getDefaultInstance();
        RequestService service = retrofit.create(RequestService.class);

        Call<ServerResponse> call = service.questionnaire(questionnaire);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.e(TAG, "onResponse: onPost: " + response.body().toString());
                EventBus.getDefault().post(QueryEvent.SUCCESS);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                EventBus.getDefault().post(QueryEvent.FAILURE);
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }

    public void photo(String userUid, Bitmap bitmap) {
        Log.i(TAG, "photo: ");
        Retrofit retrofit = getDefaultInstance();
        RequestService service = retrofit.create(RequestService.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
        final byte[] b = baos.toByteArray();
        Log.i(TAG, "photo: byte length = " + b.length);
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        PhotoRequest message = new PhotoRequest(userUid, encodedImage);
        Call<ServerResponse> call = service.post(message);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                final ServerResponse serverResponse = response.body();
                Log.e(TAG, "onResponse: onPost: " + serverResponse.toString());
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Gson gson = new Gson();
                        String json = gson.toJson(serverResponse);
                        History history = new History(System.currentTimeMillis(), b, json);
                        realm.insertOrUpdate(history);
                    }
                });
                EventBus.getDefault().post(RecipeEvent.SUCCESS);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
                EventBus.getDefault().post(RecipeEvent.FAILURE);
            }
        });
    }

    private Retrofit getDefaultInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}