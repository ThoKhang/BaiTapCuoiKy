/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.repository;

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
public interface HoatDongHocTapRepository extends JpaRepository<HoatDongHocTap, String> {

    @Query("SELECT h FROM HoatDongHocTap h WHERE h.tieuDe LIKE :pattern ORDER BY h.maHoatDong")
    List<HoatDongHocTap> findByTitlePattern(@Param("pattern") String pattern);
}

