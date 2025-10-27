package com.example.apphoctapchotre.Api;

import com.example.apphoctapchotre.model.BaiKiemTra;
import com.example.apphoctapchotre.model.CauHoiResponse;
import com.example.apphoctapchotre.model.NguoiDung;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    @GET("api/nguoi-dung")  // Sửa endpoint để khớp backend (nguoi-dung thay users)
    Call<List<NguoiDung>> getNguoiDung();

    // Nếu có thêm endpoint khác (register/login), cũng thêm "api/"
    @POST("api/register")
    Call<ResponseBody> register(@Body NguoiDung nguoiDung);

    @POST("api/login")
    Call<ResponseBody> login(@Body NguoiDung nguoiDung);
    @POST("api/verify-otp")
    Call<ResponseBody> verifyOTP(@Body Map<String, String> request);

    @POST("api/send-otp")
    Call<ResponseBody> sendOTP(@Body Map<String, String> request);
    @POST("/api/forgot-password")
    Call<ResponseBody> forgotPassword(@Body Map<String, String> body);

    @POST("/api/reset-password")
    Call<ResponseBody> resetPassword(@Body Map<String, String> body);

    @GET("/api/nguoi-dung/{id}/tongbaihoc-phanmon")
    Call<NguoiDung> getNguoiDungTongBaiHocTheoMon(@Path("id") int id);


    @GET("api/cauhoi")
    Call<List<CauHoiResponse>> getCauHoiTheoTieuDePhu(
            @Query("maMonHoc") int maMonHoc,
            @Query(value = "tieuDePhu", encoded = true) String tieuDePhu
    );

    @POST("api/hoanthanh-tieudephu")
    Call<Void> hoanThanhTieuDePhu(
            @Query("maNguoiDung") int maNguoiDung,
            @Query("maTieuDePhu") int maTieuDePhu
    );

    @GET("api/baikiemtra/cungco/{maMonHoc}/{maNguoiDung}")
    Call<List<BaiKiemTra>> getBaiKiemTraCungCoTheoMon(
            @Path("maMonHoc") int maMonHoc,
            @Path("maNguoiDung") int maNguoiDung
    );




}