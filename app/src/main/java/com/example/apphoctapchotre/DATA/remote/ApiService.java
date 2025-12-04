package com.example.apphoctapchotre.DATA.remote;

import com.example.apphoctapchotre.DATA.model.LichSuDiemResponse;
import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.DATA.model.XepHangResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
    Call<ResponseBody> sendOtp(@Body Map<String, String> body);

    @POST("api/nguoidung/reset-password")
    Call<ResponseBody> resetPassword(@Body Map<String, String> body);

    //test api cho ae
    @GET("api/ping")
    Call<ResponseBody> pingServer();

    @GET("api/nguoidung/xep-hang")
    Call<XepHangResponse> layXepHang(
            @Query("email") String email,
            @Query("gioiHan") int gioiHan
    );

    @POST("api/nguoidung/lich-su-diem")
    Call<LichSuDiemResponse> layLichSuDiem(@Body Map<String, String> body);

}
