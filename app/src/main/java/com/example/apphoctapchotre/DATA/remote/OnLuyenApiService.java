package com.example.apphoctapchotre.DATA.remote;

import retrofit2.http.GET;

import com.example.apphoctapchotre.DATA.model.OnLuyenTongQuan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OnLuyenApiService {

    @GET("api/hoatdong/on-luyen/summary")
    Call<OnLuyenTongQuan> getTongQuan(
            @Query("maNguoiDung") String maNguoiDung
    );


//    // ================= API 2: DANH SÁCH ĐỀ =================
//    @GET("api/hoatdong/on-luyen/ds")
//    Call<List<DeResponse>> getDanhSachDe(
//            @Query("loai") String loai,           // CO_BAN, TRUNG_BINH, NANG_CAO
//            @Query("maNguoiDung") String maNguoiDung
//    );
//
//
//    // ================= API 3: CHI TIẾT ĐỀ =================
//    @GET("api/hoatdong/on-luyen/cau-hoi")
//    Call<ChiTietDeResponse> getChiTietDe(
//            @Query("maHoatDong") String maHoatDong
//    );
}

