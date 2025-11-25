package com.example.apphoctapchotre.Api;

import com.example.apphoctapchotre.model.NguoiDung;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // LẤY THÔNG TIN NGƯỜI DÙNG THEO EMAIL (OPTIONAL)
    @POST("api/nguoidung/get-by-email")
    Call<NguoiDung> getByEmail(@Body Map<String, String> body);

    @POST("api/nguoidung/register")
    Call<ResponseBody> register(@Body Map<String, String> body);

    @POST("api/nguoidung/login")
    Call<ResponseBody> login(@Body Map<String, String> body);

    @POST("api/nguoidung/verify-otp")
    Call<ResponseBody> verifyOTP(@Body Map<String, String> body);

    @POST("api/nguoidung/send-otp")
    Call<ResponseBody> sendOTP(@Body Map<String, String> body);

    @POST("api/nguoidung/reset-password")
    Call<ResponseBody> resetPassword(@Body Map<String, String> body);

    //test api cho ae
    @GET("api/ping")
    Call<ResponseBody> pingServer();
}
