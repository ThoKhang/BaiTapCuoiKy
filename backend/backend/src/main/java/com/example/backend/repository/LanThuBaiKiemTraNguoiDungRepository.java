/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.backend.repository;

import com.example.backend.repository.entity.LanThuBaiKiemTraNguoiDungEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */
@Repository
public interface LanThuBaiKiemTraNguoiDungRepository extends JpaRepository<LanThuBaiKiemTraNguoiDungEntity, Integer> {
    @Query("SELECT COUNT(DISTINCT l.baiKiemTra.maBaiKiemTra) " +
       "FROM LanThuBaiKiemTraNguoiDungEntity l " +
       "WHERE l.maNguoiDung = :maNguoiDung " +
       "AND l.baiKiemTra.loaiBaiKiemTra = :loaiDe")
    int countDistinctBaiKiemTraByMaNguoiDungAndLoai(@Param("maNguoiDung") Integer maNguoiDung,@Param("loaiDe") String loaiDe);
}
