/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import com.example.backend.converter.TienTrinhConverter;
import com.example.backend.dto.request.TienTrinhRequest;
import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.entity.NguoiDung;
import com.example.backend.entity.TienTrinhHocTap;
import com.example.backend.repository.HoatDongHocTapRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import com.example.backend.service.IService.ITienTrinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class TienTrinhService implements ITienTrinhService{
    @Autowired
    private TienTrinhHocTapRepository tienTrinhrepo;
    @Autowired
    private NguoiDungRepository nguoiDungRepo;
    @Autowired HoatDongHocTapRepository hoatDongRepo;
    @Override
    public void taoTienTrinh(TienTrinhRequest request) {
        NguoiDung nguoiDung=null ;
        HoatDongHocTap hoatDongHocTap=null;
        if(request.getEmail()!=null)
            nguoiDung =nguoiDungRepo.findByEmail(request.getEmail());
        if(request.getMaHoatDong()!=null)
            hoatDongHocTap=hoatDongRepo.findById(request.getMaHoatDong()).orElse(null);
        TienTrinhHocTap tienTrinh = TienTrinhConverter.toEntity(request, nguoiDung, hoatDongHocTap);
        tienTrinhrepo.save(tienTrinh);
    }
    
}
