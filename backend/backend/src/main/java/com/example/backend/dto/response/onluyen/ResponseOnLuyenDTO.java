/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response.onluyen;

/**
 *
 * @author ADMIN
 */
public class ResponseOnLuyenDTO {
    public MucDoDTO coBan;
    public MucDoDTO trungBinh;
    public MucDoDTO nangCao;

    public ResponseOnLuyenDTO() {
    }

    public ResponseOnLuyenDTO(MucDoDTO coBan, MucDoDTO trungBinh, MucDoDTO nangCao) {
        this.coBan = coBan;
        this.trungBinh = trungBinh;
        this.nangCao = nangCao;
    }

    public MucDoDTO getCoBan() {
        return coBan;
    }

    public void setCoBan(MucDoDTO coBan) {
        this.coBan = coBan;
    }

    public MucDoDTO getTrungBinh() {
        return trungBinh;
    }

    public void setTrungBinh(MucDoDTO trungBinh) {
        this.trungBinh = trungBinh;
    }

    public MucDoDTO getNangCao() {
        return nangCao;
    }

    public void setNangCao(MucDoDTO nangCao) {
        this.nangCao = nangCao;
    }
    
}

