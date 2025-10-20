package com.example.apphoctapchotre.Api;

import com.example.apphoctapchotre.model.User;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("api/users")  // ← Thêm "api/" để khớp backend
    Call<List<User>> getUsers();

    // Nếu có thêm endpoint khác (register/login), cũng thêm "api/"
    @POST("api/register")
    Call<ResponseBody> register(@Body User user);

    @POST("api/login")
    Call<ResponseBody> login(@Body User user);
    @POST("api/verify-otp")
    Call<ResponseBody> verifyOTP(@Body Map<String, String> request);

    @POST("api/send-otp")
    Call<ResponseBody> sendOTP(@Body Map<String, String> request);
}