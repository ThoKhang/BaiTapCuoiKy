/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response.onluyen;

/**
 *
 * @author ADMIN
 */
public class OnLuyenLoaiResponse {

    public OnLuyenTongQuanDTO coBan;
    public OnLuyenTongQuanDTO trungBinh;
    public OnLuyenTongQuanDTO nangCao;

    public OnLuyenLoaiResponse() {
    }

    public OnLuyenLoaiResponse(OnLuyenTongQuanDTO coBan, OnLuyenTongQuanDTO trungBinh, OnLuyenTongQuanDTO nangCao) {
        this.coBan = coBan;
        this.trungBinh = trungBinh;
        this.nangCao = nangCao;
    }

    public OnLuyenTongQuanDTO getCoBan() {
        return coBan;
    }

    public void setCoBan(OnLuyenTongQuanDTO coBan) {
        this.coBan = coBan;
    }

    public OnLuyenTongQuanDTO getTrungBinh() {
        return trungBinh;
    }

    public void setTrungBinh(OnLuyenTongQuanDTO trungBinh) {
        this.trungBinh = trungBinh;
    }

    public OnLuyenTongQuanDTO getNangCao() {
        return nangCao;
    }

    public void setNangCao(OnLuyenTongQuanDTO nangCao) {
        this.nangCao = nangCao;
    }
    

}
