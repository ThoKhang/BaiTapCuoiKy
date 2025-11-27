package com.example.backend.dto.response;

import java.util.List;

public class XepHangResponse {

    private long tongSoNguoiChoi;
    private NguoiDungXepHangResponse nguoiDungHienTai;
    private List<NguoiDungXepHangResponse> topNguoiDung;

    public long getTongSoNguoiChoi() {
        return tongSoNguoiChoi;
    }

    public void setTongSoNguoiChoi(long tongSoNguoiChoi) {
        this.tongSoNguoiChoi = tongSoNguoiChoi;
    }

    public NguoiDungXepHangResponse getNguoiDungHienTai() {
        return nguoiDungHienTai;
    }

    public void setNguoiDungHienTai(NguoiDungXepHangResponse nguoiDungHienTai) {
        this.nguoiDungHienTai = nguoiDungHienTai;
    }

    public List<NguoiDungXepHangResponse> getTopNguoiDung() {
        return topNguoiDung;
    }

    public void setTopNguoiDung(List<NguoiDungXepHangResponse> topNguoiDung) {
        this.topNguoiDung = topNguoiDung;
    }
}
