package com.example.apphoctapchotre.Api;

import com.example.apphoctapchotre.model.BaiKiemTra;
import com.example.apphoctapchotre.model.LanThuBaiKiemTraNguoiDung;
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

    @GET("api/baikiemtra/cungco/{maMonHoc}")
    Call<List<BaiKiemTra>> getBaiKiemTraCungCo(@Path("maMonHoc") int maMonHoc);

    @GET("api/baikiemtra/cauhoi/{maBaiKiemTra}")
    Call<List<Map<String, Object>>> getCauHoiForBaiKiemTra(
            @Path("maBaiKiemTra") int maBaiKiemTra,
            @Query("shuffle") boolean shuffle
    );

    @POST("api/baikiemtra/submit")
    Call<LanThuBaiKiemTraNguoiDung> submitBaiKiemTra(
            @Query("maNguoiDung") int maNguoiDung,
            @Query("maBaiKiemTra") int maBaiKiemTra,
            @Body Map<Integer, Integer> dapAnMap
    );
}
