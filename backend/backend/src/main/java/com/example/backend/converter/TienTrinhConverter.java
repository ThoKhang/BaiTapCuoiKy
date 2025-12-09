/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.converter;

import com.example.backend.dto.request.TienTrinhRequest;
import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.entity.NguoiDung;
import com.example.backend.entity.TienTrinhHocTap;
import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class TienTrinhConverter {
    public static TienTrinhHocTap toEntity(TienTrinhRequest req,NguoiDung nguoiDung,HoatDongHocTap hoatDong) {
        TienTrinhHocTap tt = new TienTrinhHocTap();
        tt.setMaTienTrinh("TT" + System.currentTimeMillis());
        tt.setNguoiDung(nguoiDung);
        tt.setHoatDong(hoatDong);
        tt.setNgayBatDau(req.getNgayBatDau() != null ? req.getNgayBatDau() : LocalDateTime.now());
        tt.setNgayHoanThanh(LocalDateTime.now());
        tt.setSoCauDung(req.getSoCauDung());
        tt.setSoCauDaLam(req.getSoCauDaLam());
        tt.setDiemDatDuoc(req.getDiemDatDuoc());
        tt.setDaHoanThanh(req.getDaHoanThanh() == 1);
        return tt;
    }
}
