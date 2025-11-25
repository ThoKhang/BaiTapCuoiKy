package com.example.backend.converter;

import com.example.backend.entity.NguoiDung;
import com.example.backend.dto.response.NguoiDungResponse;

public class NguoiDungConverter {

    public static NguoiDungResponse toResponse(NguoiDung entity) {
        NguoiDungResponse dto = new NguoiDungResponse();
        dto.setMaNguoiDung(entity.getMaNguoiDung());
        dto.setTenDangNhap(entity.getTenDangNhap());
        dto.setEmail(entity.getEmail());
        dto.setTongDiem(entity.getTongDiem());
        dto.setNgayTao(entity.getNgayTao());
        return dto;
    }
}
