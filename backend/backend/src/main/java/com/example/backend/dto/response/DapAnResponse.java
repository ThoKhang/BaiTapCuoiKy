/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response;

/**
 *
 * @author ADMIN
 */
public class DapAnResponse {
    private String MaDapAn;
    private String MaCauHoi;
    private String NoiDungDapAn;
    private boolean LaDapAnDung;

    public String getMaDapAn() {
        return MaDapAn;
    }

    public void setMaDapAn(String MaDapAn) {
        this.MaDapAn = MaDapAn;
    }

    public String getMaCauHoi() {
        return MaCauHoi;
    }

    public void setMaCauHoi(String MaCauHoi) {
        this.MaCauHoi = MaCauHoi;
    }

    public String getNoiDungDapAn() {
        return NoiDungDapAn;
    }

    public void setNoiDungDapAn(String NoiDungDapAn) {
        this.NoiDungDapAn = NoiDungDapAn;
    }

    public boolean getLaDapAnDung() {
        return LaDapAnDung;
    }

    public void setLaDapAnDung(boolean LaDapAnDung) {
        this.LaDapAnDung = LaDapAnDung;
    }
    
    
}
