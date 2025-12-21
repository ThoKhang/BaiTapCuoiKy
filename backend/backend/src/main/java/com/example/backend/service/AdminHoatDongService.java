package com.example.backend.service;

import com.example.backend.entity.HoatDongCauHoi;
import com.example.backend.entity.HoatDongHocTap;
import com.example.backend.repository.HoatDongCauHoiRepository;
import com.example.backend.repository.HoatDongHocTapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminHoatDongService {

    private final HoatDongHocTapRepository hoatDongRepo;
    private final HoatDongCauHoiRepository hoatDongCauHoiRepo;

    public AdminHoatDongService(HoatDongHocTapRepository hoatDongRepo,
                                HoatDongCauHoiRepository hoatDongCauHoiRepo) {
        this.hoatDongRepo = hoatDongRepo;
        this.hoatDongCauHoiRepo = hoatDongCauHoiRepo;
    }

    public List<HoatDongHocTap> getAll() {
        return hoatDongRepo.findAll();
    }

    public HoatDongHocTap getById(String id) {
        return hoatDongRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hoạt động"));
    }

    public HoatDongHocTap create(HoatDongHocTap hd) {
        if (hoatDongRepo.existsById(hd.getMaHoatDong())) {
            throw new RuntimeException("Mã hoạt động đã tồn tại");
        }
        return hoatDongRepo.save(hd);
    }

    public HoatDongHocTap update(String id, HoatDongHocTap hd) {
        HoatDongHocTap old = getById(id);
        old.setTieuDe(hd.getTieuDe());
        old.setMoTa(hd.getMoTa());
        old.setTongDiemToiDa(hd.getTongDiemToiDa());
        old.setMaMonHoc(hd.getMaMonHoc());
        old.setMaLoai(hd.getMaLoai());
        return hoatDongRepo.save(old);
    }

    public void delete(String id) {
        hoatDongRepo.deleteById(id);
    }

    // Gán câu hỏi cho hoạt động
    public void addCauHoi(String maHoatDong, String maCauHoi, int thuTu) {
        HoatDongCauHoi link = new HoatDongCauHoi();
        link.setMaHoatDong(maHoatDong);
        link.setMaCauHoi(maCauHoi);
        link.setThuTu(thuTu);
        hoatDongCauHoiRepo.save(link);
    }

}
