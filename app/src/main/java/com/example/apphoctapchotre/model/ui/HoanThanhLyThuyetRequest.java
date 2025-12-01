package com.example.apphoctapchotre.model.ui;

import com.google.gson.annotations.SerializedName;

public class HoanThanhLyThuyetRequest {

    @SerializedName("maHoatDong")
    private String maHoatDong;

    @SerializedName("email")
    private String email;

    // Constructor bắt buộc
    public HoanThanhLyThuyetRequest(String maHoatDong, String email) {
        this.maHoatDong = maHoatDong;
        this.email = email;
    }

    // Getter (bắt buộc để Retrofit/Gson serialize)
    public String getMaHoatDong() {
        return maHoatDong;
    }

    public String getEmail() {
        return email;
    }

    // Setter (tùy chọn nhưng nên có)
    public void setMaHoatDong(String maHoatDong) {
        this.maHoatDong = maHoatDong;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}