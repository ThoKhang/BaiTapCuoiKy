/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.repository;

import com.example.backend.entity.TienTrinhHocTap;
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
public interface TienTrinhHocTapRepository extends JpaRepository<TienTrinhHocTap, String> {
    @Query("SELECT t FROM TienTrinhHocTap t WHERE t.nguoiDung.maNguoiDung = :userId")
    List<TienTrinhHocTap> findByNguoiDung(@Param("userId") String userId);
}

