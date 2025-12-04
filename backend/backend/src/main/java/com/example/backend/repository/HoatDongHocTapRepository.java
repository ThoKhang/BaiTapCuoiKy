package com.example.backend.repository;

import com.example.backend.entity.HoatDongHocTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoatDongHocTapRepository extends JpaRepository<HoatDongHocTap, String> {

    @Query("SELECT h FROM HoatDongHocTap h WHERE h.maMonHoc = ?1 AND h.maLoai = 'LHD02'")
    List<HoatDongHocTap> getCungCoByMonHoc(String maMonHoc);
}
