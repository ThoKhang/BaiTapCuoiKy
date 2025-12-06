package com.example.apphoctapchotre.DATA.remote;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;

public class RetrofitClient {

    // BASE_URL: dùng IP máy bạn (10.0.2.2 là localhost của emulator)
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    // Nếu chạy trên máy thật, đổi sang IP trong mạng LAN:
//    private static final String BASE_URL = "http://172.20.10.8:8080/";

    private static Retrofit retrofit;

    // Thêm Context để đọc SharedPreferences
    public static Retrofit getClient(Context context) {
        // Thêm Interceptor chứa token
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(context))
                .build();
        if (retrofit == null) {
            Gson gson = GsonProvider.getGson();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)   // GẮN CLIENT CÓ TOKEN
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
