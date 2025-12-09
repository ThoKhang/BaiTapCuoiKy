/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import com.example.backend.dto.response.OnLuyenResponse;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.HoatDongHocTapRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import com.example.backend.service.IService.IOnLuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class OnLuyenService implements IOnLuyenService{
    @Autowired
    private HoatDongHocTapRepository hoatdonghoctap ;
    @Autowired
    private TienTrinhHocTapRepository tientrinhhoctap;
    @Autowired
    private NguoiDungRepository nguoidung;
    @Override
    public OnLuyenResponse layThongTinOnLuyen(String email) {
        OnLuyenResponse onLuyenResponse = new OnLuyenResponse();
        NguoiDung nd = nguoidung.findByEmail(email);
        if(nd==null)
            throw new RuntimeException("Email không tồn tại!");
        String maNguoiDung=nd.getMaNguoiDung();
        onLuyenResponse.setTongSoDeCoBan(hoatdonghoctap.tongSoDeCoBan());
        onLuyenResponse.setTongSoDeTrungBinh(hoatdonghoctap.tongSoDeTrungBinh());
        onLuyenResponse.setTongSoDeNangCao(hoatdonghoctap.tongSoDeNangCao());
        onLuyenResponse.setSoDeCoBanDaLam(tientrinhhoctap.soDeCoBanDaLam(maNguoiDung));
        onLuyenResponse.setSoDeTrungBinhDaLam(tientrinhhoctap.soDeTrungBinhDaLam(maNguoiDung));
        onLuyenResponse.setSoDeNangCaoDaLam(tientrinhhoctap.soDeNangCaoDaLam(maNguoiDung));
        return onLuyenResponse;
    }
    
}
