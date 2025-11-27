package com.example.backend.repository;

import com.example.backend.entity.LichSuHoatDong;
import com.example.backend.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LichSuHoatDongRepository extends JpaRepository<LichSuHoatDong, String> {

    List<LichSuHoatDong> findByNguoiDungOrderByThoiGianDesc(NguoiDung nguoiDung);

    @Query("SELECT COALESCE(SUM(l.soDiem),0) " +
           "FROM LichSuHoatDong l " +
           "WHERE l.nguoiDung.maNguoiDung = :maNguoiDung " +
           "AND l.loaiDiem = :loaiDiem")
    int tongDiemTheoLoai(@Param("maNguoiDung") String maNguoiDung,
                         @Param("loaiDiem") String loaiDiem);
    
    //tổng tất cả điểm (mọi loại điểm)
    @Query("SELECT COALESCE(SUM(l.soDiem), 0) " +
           "FROM LichSuHoatDong l " +
           "WHERE l.nguoiDung.maNguoiDung = :maNguoiDung")
    int tongDiemTatCa(@Param("maNguoiDung") String maNguoiDung);
}   
