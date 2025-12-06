/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.backend.service;

import com.example.backend.dto.response.onluyen.DeDTO;
import com.example.backend.dto.response.onluyen.OnLuyenLoaiResponse;
import com.example.backend.dto.response.onluyen.OnLuyenTongQuanDTO;
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
public class HoatDongHocTapService implements IHoatDongHocTapService {

    @Autowired
    HoatDongHocTapRepository hoatDongRepo;

    @Autowired
    TienTrinhHocTapRepository tienTrinhRepo;

    @Autowired
    HoatDongCauHoiRepository cauHoiRepo;


    // ================= API 1: TỔNG QUAN =================
    @Override
    public OnLuyenLoaiResponse getTongQuan(String userId) {

        List<TienTrinhHocTap> tienTrinh = tienTrinhRepo.findByNguoiDung(userId);
        Set<String> dsDaLam = tienTrinh.stream()
                .map(t -> t.getHoatDong().getMaHoatDong())
                .collect(Collectors.toSet());

        OnLuyenLoaiResponse response = new OnLuyenLoaiResponse();
            response.coBan= tinhTongQuan("Ôn Cơ bản%", dsDaLam);
            response.trungBinh= tinhTongQuan("Ôn TB%", dsDaLam);
            response.nangCao= tinhTongQuan("Ôn NC%", dsDaLam);

        return response;
    }

    private OnLuyenTongQuanDTO tinhTongQuan(String pattern, Set<String> dsDaLam) {
        List<HoatDongHocTap> list = hoatDongRepo.findByTitlePattern(pattern);

        int tongDe = list.size();
        int tongDaLam = 0;

        for (HoatDongHocTap hd : list) {
            if (dsDaLam.contains(hd.getMaHoatDong())) tongDaLam++;
        }

        return new OnLuyenTongQuanDTO(tongDe, tongDaLam);
    }


//    // ================= API 2: DANH SÁCH ĐỀ =================
//    @Override
//    public List<DeDTO> getDanhSachDe(String loai, String userId) {
//
//        String pattern =
//            loai.equals("CO_BAN")      ? "Ôn Cơ bản%" :
//            loai.equals("TRUNG_BINH")  ? "Ôn TB%" :
//            loai.equals("NANG_CAO")    ? "Ôn NC%" : null;
//
//        if (pattern == null) return new ArrayList<>();
//
//        List<TienTrinhHocTap> tienTrinh = tienTrinhRepo.findByNguoiDung(userId);
//        Set<String> dsDaLam = tienTrinh.stream()
//                .map(t -> t.getHoatDong().getMaHoatDong())
//                .collect(Collectors.toSet());
//
//        List<HoatDongHocTap> ds = hoatDongRepo.findByTitlePattern(pattern);
//
//        List<DeDTO> result = new ArrayList<>();
//        for (HoatDongHocTap hd : ds) {
//
//            DeDTO dto = new DeDTO();
//            dto.maHoatDong = hd.getMaHoatDong();
//            dto.tieuDe = hd.getTieuDe();
//            dto.soCauHoi = cauHoiRepo.countCauHoi(hd.getMaHoatDong());
//            dto.daLam = dsDaLam.contains(hd.getMaHoatDong());
//
//            result.add(dto);
//        }
//
//        return result;
//    }
//
//
//    // ================= API 3: CHI TIẾT ĐỀ =================
//    @Override
//    public ChiTietDeDTO getChiTietDe(String maHoatDong) {
//
//        HoatDongHocTap hd = hoatDongRepo.findById(maHoatDong)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy hoạt động"));
//
//        List<HoatDongCauHoi> cauHoiList = cauHoiRepo.findByMaHoatDong(maHoatDong);
//
//        ChiTietDeDTO dto = new ChiTietDeDTO();
//        dto.maHoatDong = hd.getMaHoatDong();
//        dto.tieuDe = hd.getTieuDe();
//        dto.soCauHoi = cauHoiList.size();
//        dto.cauHoi = new ArrayList<>();
//
//        for (HoatDongCauHoi hqc : cauHoiList) {
//
//            CauHoi ch = hqc.getCauHoi();
//
//            CauHoiDTO chDto = new CauHoiDTO();
//            chDto.maCauHoi = ch.getMaCauHoi();
//            chDto.noiDung = ch.getNoiDungCauHoi();
//            chDto.giaiThich = ch.getGiaiThich();
//            chDto.thuTu = hqc.getThuTu();
//            chDto.dapAn = new ArrayList<>();
//
//            for (DapAn da : ch.getDapAnList()) {
//                DapAnDTO daDto = new DapAnDTO();
//                daDto.maDapAn = da.getMaDapAn();
//                daDto.noiDung = da.getNoiDungDapAn();
//                daDto.dung = da.getLaDapAnDung() == 1;
//
//                chDto.dapAn.add(daDto);
//            }
//
//            dto.cauHoi.add(chDto);
//        }
//
//        return dto;
//    }

    @Override
    public List<DeDTO> getDanhSachDe(String loai, String maNguoiDung) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
