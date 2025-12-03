/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import com.example.backend.dto.response.onluyen.DeDTO;
import com.example.backend.dto.response.onluyen.MucDoDTO;
import com.example.backend.dto.response.onluyen.ResponseOnLuyenDTO;
import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.entity.TienTrinhHocTap;
import com.example.backend.repository.HoatDongCauHoiRepository;
import com.example.backend.repository.HoatDongHocTapRepository;
import com.example.backend.repository.TienTrinhHocTapRepository;
import com.example.backend.service.IService.IHoatDongHocTapService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class HoatDongHocTapService implements IHoatDongHocTapService{
     @Autowired
    private HoatDongHocTapRepository hoatDongRepo;

    @Autowired
    private TienTrinhHocTapRepository tienTrinhRepo;

    @Autowired
    private HoatDongCauHoiRepository cauHoiRepo;
    
    @Override
    public ResponseOnLuyenDTO layThongTinOnLuyen(String maNguoiDung) {

        List<TienTrinhHocTap> tienTrinh = tienTrinhRepo.findByNguoiDung(maNguoiDung);

        // convert tiến trình sang map để check nhanh
        Set<String> dsDaLam = tienTrinh.stream()
                .map(t -> t.getHoatDong().getMaHoatDong())
                .collect(Collectors.toSet());

        ResponseOnLuyenDTO res = new ResponseOnLuyenDTO();

        res.coBan = taoMucDo("Ôn Cơ bản%", dsDaLam);
        res.trungBinh = taoMucDo("Ôn TB%", dsDaLam);
        res.nangCao = taoMucDo("Ôn NC%", dsDaLam);

        return res;
    }
    
    @Override
    public MucDoDTO taoMucDo(String pattern, Set<String> dsDaLam) {

        List<HoatDongHocTap> dsDe = hoatDongRepo.findByTieuDePattern(pattern);

        MucDoDTO md = new MucDoDTO();
        md.tongDe = dsDe.size();
        md.danhSach = new ArrayList<>();

        int daLamCount = 0;

        for (HoatDongHocTap de : dsDe) {
            DeDTO dto = new DeDTO();
            dto.maHoatDong = de.getMaHoatDong();
            dto.tieuDe = de.getTieuDe();
            dto.soCauHoi = cauHoiRepo.countCauHoi(de.getMaHoatDong());

            dto.daLam = dsDaLam.contains(de.getMaHoatDong());
            if (dto.daLam) daLamCount++;

            md.danhSach.add(dto);
        }

        md.tongDeDaLam = daLamCount;

        return md;
    }
    
}
