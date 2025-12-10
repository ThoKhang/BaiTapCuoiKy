/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import com.example.backend.converter.TienTrinhConverter;
import com.example.backend.dto.request.TienTrinhRequest;
import com.example.backend.dto.response.TienTrinhResponse;
import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.entity.NguoiDung;
import com.example.backend.entity.TienTrinhHocTap;
import com.example.backend.repository.HoatDongHocTapRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import com.example.backend.service.IService.ITienTrinhService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class TienTrinhService implements ITienTrinhService {

    @Autowired
    private TienTrinhHocTapRepository tienTrinhrepo;
    @Autowired
    private NguoiDungRepository nguoiDungRepo;
    @Autowired 
    private HoatDongHocTapRepository hoatDongRepo;
    @Override
    public void taoTienTrinh(TienTrinhRequest request) {
        NguoiDung nguoiDung = null;
        if (request.getEmail() != null)
            nguoiDung = nguoiDungRepo.findByEmail(request.getEmail());
        HoatDongHocTap hoatDong = null;
        if (request.getMaHoatDong() != null)
            hoatDong = hoatDongRepo.findById(request.getMaHoatDong()).orElse(null);
        TienTrinhHocTap tienTrinhTonTai =tienTrinhrepo.findByEmailAndHoatDong(request.getEmail(), request.getMaHoatDong());
        if (tienTrinhTonTai != null) {
            tienTrinhTonTai.setSoCauDung(request.getSoCauDung());
            tienTrinhTonTai.setSoCauDaLam(request.getSoCauDaLam());
            tienTrinhTonTai.setDiemDatDuoc(request.getDiemDatDuoc());
            tienTrinhTonTai.setDaHoanThanh(request.getDaHoanThanh() == 1);
            tienTrinhTonTai.setNgayHoanThanh(LocalDateTime.now());
            tienTrinhrepo.save(tienTrinhTonTai);
            return; 
        }
        TienTrinhHocTap tienTrinh = TienTrinhConverter.toEntity(request, nguoiDung, hoatDong);
        String idLonNhat = tienTrinhrepo.findIdLonNhat(); 
        int soMoi = 1;
        if (idLonNhat != null) {
            soMoi = Integer.parseInt(idLonNhat.substring(2)) + 1;
        }
        tienTrinh.setMaTienTrinh(String.format("TT%03d", soMoi));
        tienTrinhrepo.save(tienTrinh);
    }
    @Override
    public List<TienTrinhResponse> getCauDungAndDaLam(String email, String tieuDe) {
        List<TienTrinhHocTap> list = tienTrinhrepo.findByEmailAndTieuDeHoatDong(email, tieuDe);
        List<TienTrinhResponse> result = new ArrayList<>();
        if (list != null) {
            for (TienTrinhHocTap tt : list) {
                result.add(TienTrinhConverter.toResponse(tt));
            }
        }
        return result;
    }
}