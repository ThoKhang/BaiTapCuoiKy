/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.example.backend.service;

import com.example.backend.repository.BaiKiemTraRepository;
import com.example.backend.repository.LanThuBaiKiemTraNguoiDungRepository;
import com.example.backend.service.IService.IDeKiemTraService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class DeKiemTraService implements IDeKiemTraService{

    @Autowired
    private  BaiKiemTraRepository baiKiemTraRepo;
    @Autowired
    private LanThuBaiKiemTraNguoiDungRepository lanThuRepo;
    
    @Override
    public int getTongSoDeTheoLoai(String loaiDe) {
        return (int) baiKiemTraRepo.countByLoaiBaiKiemTra(loaiDe);
    }

    @Override
    public int getSoDeDaLamTheoLoai(Integer maNguoiDung, String loaiDe) {
        return lanThuRepo.countDistinctBaiKiemTraByMaNguoiDungAndLoai(maNguoiDung, loaiDe);
    }

    @Override
    public Map<String, String> getTienDoOnLuyen(Integer maNguoiDung) {
      Map<String, String> tienDo = new HashMap<>();
        String loaiCoBan = "OnTapCoBan";
        String loaiTrungBinh = "OnTapTrungBinh";
        String loaiNangCao = "OnTapNangCao";
        int daLamCB = getSoDeDaLamTheoLoai(maNguoiDung, loaiCoBan);
        int tongCB = getTongSoDeTheoLoai(loaiCoBan);
        tienDo.put("CoBan", daLamCB + "/" + tongCB);
        
        int daLamTB = getSoDeDaLamTheoLoai(maNguoiDung, loaiTrungBinh);
        int tongTB = getTongSoDeTheoLoai(loaiTrungBinh);
        tienDo.put("TrungBinh", daLamTB + "/" + tongTB);
        
        int daLamNC = getSoDeDaLamTheoLoai(maNguoiDung, loaiNangCao);
        int tongNC = getTongSoDeTheoLoai(loaiNangCao);
        tienDo.put("NangCao", daLamNC + "/" + tongNC);
        
        return tienDo;
    }
}
