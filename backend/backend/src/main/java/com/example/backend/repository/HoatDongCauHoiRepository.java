/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.repository;

import com.example.backend.entity.HoatDongCauHoi;
import com.example.backend.entity.HoatDongCauHoiKey;
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

    @Query("SELECT COUNT(h) FROM HoatDongCauHoi h WHERE h.hoatDong.maHoatDong = :maHoatDong")
    Integer countCauHoi(@Param("maHoatDong") String maHoatDong);
}

