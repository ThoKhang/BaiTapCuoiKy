package com.example.backend.repository;

import com.example.backend.entity.TienTrinhHocTap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TienTrinhHocTapRepository extends JpaRepository<TienTrinhHocTap, String> {

    Optional<TienTrinhHocTap> findByNguoiDung_MaNguoiDungAndHoatDong_MaHoatDong(
            String maNguoiDung,
            String maHoatDong
    );

    // Lấy tất cả tiến trình của 1 người dùng
    List<TienTrinhHocTap> findByNguoiDung_MaNguoiDung(String maNguoiDung);
}

