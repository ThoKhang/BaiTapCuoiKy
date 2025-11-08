package com.example.apphoctapchotre.Api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = GsonProvider.getGson();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
