package com.example.backend.converter;

import com.example.backend.config.NguoiDungDetails;
import com.example.backend.entity.NguoiDung;
import com.example.backend.dto.response.NguoiDungResponse;
import org.springframework.security.core.userdetails.UserDetails;

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
    public static UserDetails toUserDetails(NguoiDung entity) {
        return new NguoiDungDetails(entity); 
    }
}
