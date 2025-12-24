package com.example.apphoctapchotre.DATA.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;

public class RetrofitClient {

    // BASE_URL: dùng IP máy bạn (10.0.2.2 là localhost của emulator)
    //private static final String BASE_URL = "http://172.26.99.215:8080/";
    //private static final String BASE_URL = "http://10.0.2.2:8080/";
    // Nếu chạy trên máy thật, đổi sang IP trong mạng LAN:
    private static final String BASE_URL = "http://172.20.10.4:8080/";

    private static Retrofit retrofit;

    // ⚠️ KHÔNG còn @RequiresApi ở đây nữa
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
