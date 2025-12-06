/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.repository;

import com.example.backend.entity.HoatDongCauHoi;
import com.example.backend.entity.HoatDongCauHoiKey;
import com.example.backend.entity.HoatDongHocTap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */
@Repository
public interface HoatDongCauHoiRepository extends JpaRepository<HoatDongCauHoi, HoatDongCauHoiKey> {

    @Query("SELECT COUNT(hqc) FROM HoatDongCauHoi hqc WHERE hqc.hoatDong.maHoatDong = :maHD")
    int countCauHoi(@Param("maHD") String maHD);

    @Query("SELECT hqc FROM HoatDongCauHoi hqc WHERE hqc.hoatDong.maHoatDong = :maHD ORDER BY hqc.thuTu")
    List<HoatDongCauHoi> findByMaHoatDong(@Param("maHD") String maHD);
}

