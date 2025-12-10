/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.converter;

import com.example.backend.dto.request.TienTrinhRequest;
import com.example.backend.dto.response.TienTrinhResponse;
import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.entity.NguoiDung;
import com.example.backend.entity.TienTrinhHocTap;
import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class TienTrinhConverter {
    public static TienTrinhHocTap toEntity(TienTrinhRequest req, NguoiDung nguoiDung, HoatDongHocTap hoatDong) {
        TienTrinhHocTap tt = new TienTrinhHocTap();
        tt.setNguoiDung(nguoiDung);
        tt.setHoatDong(hoatDong);
        tt.setNgayBatDau(LocalDateTime.now());
        tt.setNgayHoanThanh(LocalDateTime.now());
        tt.setSoCauDung(req.getSoCauDung());
        tt.setSoCauDaLam(req.getSoCauDaLam());
        tt.setDiemDatDuoc(req.getDiemDatDuoc());
        tt.setDaHoanThanh(req.getDaHoanThanh() == 1);
        return tt;
    }
    public static TienTrinhResponse toResponse(TienTrinhHocTap tt){
        TienTrinhResponse response = new TienTrinhResponse();
        response.setSoCauDung(tt.getSoCauDung());
        response.setSoCauDaLam(tt.getSoCauDaLam());
        response.setTieuDe(tt.getHoatDong().getTieuDe());
        return response;
    }
}

