package com.example.apphoctapchotre.Api;

import com.example.apphoctapchotre.model.NguoiDung;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("api/nguoi-dung")
    Call<List<NguoiDung>> getNguoiDung();

    @POST("api/register")
    Call<ResponseBody> register(@Body NguoiDung nguoiDung);

    @POST("api/login")
    Call<ResponseBody> login(@Body NguoiDung nguoiDung);

    @POST("api/verify-otp")
    Call<ResponseBody> verifyOTP(@Body Map<String, String> request);

    @POST("api/send-otp")
    Call<ResponseBody> sendOTP(@Body Map<String, String> request);

    @POST("api/forgot-password")
    Call<ResponseBody> forgotPassword(@Body Map<String, String> body);

    @POST("api/reset-password")
    Call<ResponseBody> resetPassword(@Body Map<String, String> body);
}
