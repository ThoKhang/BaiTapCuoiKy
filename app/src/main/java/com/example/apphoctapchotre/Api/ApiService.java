package com.example.apphoctapchotre.Api;

import com.example.apphoctapchotre.model.LichSuDiemResponse;
import com.example.apphoctapchotre.model.NguoiDung;
import com.example.apphoctapchotre.model.XepHangResponse;
import com.example.apphoctapchotre.model.ui.HoanThanhLyThuyetRequest;
import com.example.apphoctapchotre.model.ui.LyThuyetDetailResponse;
import com.example.apphoctapchotre.model.ui.LyThuyetItemResponse;
import java.util.Map;
import java.util.List;

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
    
    @GET("api/lythuyet/danh-sach")
    Call<List<LyThuyetItemResponse>> getDanhSach(
            @Query("email") String email,
            @Query("mon") String maMonHoc);   // MH001 = Toán, MH002 = Tiếng Việt
    
    @GET("api/lythuyet/chi-tiet")
    Call<LyThuyetDetailResponse> getChiTietLyThuyet(
            @Query("maHoatDong") String maHoatDong,
            @Query("email") String email
    );

    @POST("api/lythuyet/hoan-thanh")
    Call<ResponseBody> hoanThanh(@Body HoanThanhLyThuyetRequest request);
}
