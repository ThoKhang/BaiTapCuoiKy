package com.example.apphoctapchotre.DATA.remote;

import com.example.apphoctapchotre.DATA.model.CauHoi;
import com.example.apphoctapchotre.DATA.model.CauHoiDapAnResponse;
import com.example.apphoctapchotre.DATA.model.DeOnLuyen;
import com.example.apphoctapchotre.DATA.model.LichSuDiemResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetDaLamResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetMonHocResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetNoiDungResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetResponse;
import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.DATA.model.OnLuyen;
import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.DATA.model.XepHangResponse;
import com.example.apphoctapchotre.DATA.model.CungCoResponse;
import com.example.apphoctapchotre.DATA.model.CungCoMonHocResponse;
import com.example.apphoctapchotre.DATA.model.CungCoDaLamResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

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

    @GET("cungco/tiendo/{maNguoiDung}")
    Call<List<CungCoResponse>> getTienDo(@Path("maNguoiDung") String maNguoiDung);

    @GET("cungco/monhoc/{maMonHoc}")
    Call<List<CungCoMonHocResponse>> getDanhSachCungCo(
            @Path("maMonHoc") String maMonHoc
    );

    @GET("cungco/dalams")
    Call<List<CungCoDaLamResponse>> getCungCoDaLam(
            @Query("maMonHoc") String maMonHoc,
            @Query("maNguoiDung") String maNguoiDung
    );

    @GET("cauhoi/{maCauHoi}")
    Call<CauHoi> getCauHoiById(@Path("maCauHoi") String maCauHoi);

    @GET("cauhoi/bailam/{maHoatDong}")
    Call<List<CauHoi>> getDanhSachCauHoi(@Path("maHoatDong") String maHoatDong);

    @GET("cauhoi/bailam/{maBaiLam}")
    Call<List<CauHoiDapAnResponse>> getCauHoiBaiLam(@Path("maBaiLam") String maBaiLam);

    @POST("cungco/hoanthanh")
    Call<Void> hoanThanh(
            @Query("maNguoiDung") String maNguoiDung,
            @Query("maHoatDong") String maHoatDong,
            @Query("soCauDung") int soCauDung,
            @Query("tongCauHoi") int tongCauHoi,
            @Query("diem") int diem
    );
    @GET("lythuyet/tiendo/{maNguoiDung}")
    Call<List<LyThuyetResponse>> getTienDoLyThuyet(@Path("maNguoiDung") String maNguoiDung);


    @GET("lythuyet/monhoc/{maMonHoc}")
    Call<List<LyThuyetMonHocResponse>> getDanhSachLyThuyet(
            @Path("maMonHoc") String maMonHoc
    );

    @GET("lythuyet/dalams")
    Call<List<LyThuyetDaLamResponse>> getLyThuyetDaLam(
            @Query("maMonHoc") String maMonHoc,
            @Query("maNguoiDung") String maNguoiDung
    );
    @GET("lythuyet-noidung/{maHoatDong}")
    Call<LyThuyetNoiDungResponse> getNoiDungLyThuyet(@Path("maHoatDong") String maHoatDong);
    @POST("lythuyet/hoanthanh")
    Call<Void> hoanThanhLyThuyet(
            @Query("maNguoiDung") String maNguoiDung,
            @Query("maHoatDong") String maHoatDong,
            @Query("diem") int diem  // Loại soCauDung, tongCauHoi
    );
    @GET("api/onluyen")
    Call<OnLuyen> onLuyen(@Query("email") String email);
    @GET("cauhoi/onluyen-coban")
    Call<DeOnLuyen> deOnLuyen(@Query("tieuDe") String tieuDe);
    @POST("api/tientrinh/add")
    Call<Void> taoTienTrinh(@Body TienTrinh tienTrinh);
    @POST("api/nguoidung/login-google")
    Call<NguoiDung> loginWithGoogle(@Body Map<String, String> body);

    @GET("api/tientrinh/socaudalam-onluyen")
    Call<List<TienTrinh>> soCauDaLam(@Query("email") String email, @Query("tieuDe") String tieuDe);
    @GET("cauhoi/deHoanThienCH")
    Call<DeOnLuyen> deHoanThien();
    @GET("cauhoi/trumTinhNham")
    Call<DeOnLuyen> deTinhNham();
}