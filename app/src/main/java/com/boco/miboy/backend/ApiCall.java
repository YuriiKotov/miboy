package com.boco.miboy.backend;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.boco.miboy.enums.QueryEvent;
import com.boco.miboy.model.PhotoRequest;
import com.boco.miboy.model.Questionnaire;
import com.boco.miboy.model.Registration;
import com.boco.miboy.enums.AuthEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
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
        Retrofit retrofit = getDefaultInstance();
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

    public void photo(String userUid, Bitmap bitmap) {
        Log.i(TAG, "photo: ");
        Retrofit retrofit = getDefaultInstance();
        RequestService service = retrofit.create(RequestService.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        PhotoRequest message = new PhotoRequest(userUid, encodedImage);
        Call<ResponseBody> call = service.post(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: onPost: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: onPost: " + t.getMessage());
            }
        });
    }

    private Retrofit getDefaultInstance() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}