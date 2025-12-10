/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.dto.response;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CauHoiResponse {
    private String MaCauHoi;
    private String NoiDungCauHoi;
    private String GiaiThich;
    private int DiemToiDa;
    private List<DapAnResponse> dapAn;

    public String getMaCauHoi() {
        return MaCauHoi;
    }

    public void setMaCauHoi(String MaCauHoi) {
        this.MaCauHoi = MaCauHoi;
    }

    public String getNoiDungCauHoi() {
        return NoiDungCauHoi;
    }

    public void setNoiDungCauHoi(String NoiDungCauHoi) {
        this.NoiDungCauHoi = NoiDungCauHoi;
    }

    public String getGiaiThich() {
        return GiaiThich;
    }

    public void setGiaiThich(String GiaiThich) {
        this.GiaiThich = GiaiThich;
    }

    public int getDiemToiDa() {
        return DiemToiDa;
    }

    public void setDiemToiDa(int DiemToiDa) {
        this.DiemToiDa = DiemToiDa;
    }

    public List<DapAnResponse> getDapAn() {
        return dapAn;
    }

    public void setDapAn(List<DapAnResponse> dapAn) {
        this.dapAn = dapAn;
    }
    
    
}
