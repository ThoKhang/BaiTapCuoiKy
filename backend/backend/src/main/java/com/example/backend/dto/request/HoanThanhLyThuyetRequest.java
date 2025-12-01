package com.example.backend.dto.request;

public class HoanThanhLyThuyetRequest {
    private String maHoatDong;
    private String email;

    public String getMaHoatDong() { return maHoatDong; }
    public void setMaHoatDong(String maHoatDong) { this.maHoatDong = maHoatDong; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}