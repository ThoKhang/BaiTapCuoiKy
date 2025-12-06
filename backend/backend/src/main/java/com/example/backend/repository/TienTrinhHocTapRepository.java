package com.example.backend.repository;

import com.example.backend.entity.TienTrinhHocTap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TienTrinhHocTapRepository extends JpaRepository<TienTrinhHocTap, String> {

    Optional<TienTrinhHocTap> findByNguoiDung_MaNguoiDungAndHoatDong_MaHoatDong(
            String maNguoiDung,
            String maHoatDong
    );
}
