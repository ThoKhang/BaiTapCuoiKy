package com.example.backend.service;

import com.example.backend.entity.CauHoi;
import com.example.backend.entity.DapAn;
import com.example.backend.repository.CauHoiRepository;
import com.example.backend.repository.DapAnRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCauHoiService {

    private final CauHoiRepository cauHoiRepo;
    private final DapAnRepository dapAnRepo;

    public AdminCauHoiService(CauHoiRepository cauHoiRepo,
                              DapAnRepository dapAnRepo) {
        this.cauHoiRepo = cauHoiRepo;
        this.dapAnRepo = dapAnRepo;
    }

    // ===== CÂU HỎI =====
    public List<CauHoi> getAllCauHoi() {
        return cauHoiRepo.findAll();
    }

    public CauHoi createCauHoi(CauHoi ch) {
        return cauHoiRepo.save(ch);
    }

    public void deleteCauHoi(String id) {
        cauHoiRepo.deleteById(id);
    }

    // ===== ĐÁP ÁN =====
    public List<DapAn> getDapAnByCauHoi(String maCauHoi) {
        return dapAnRepo.findByMaCauHoi(maCauHoi);
    }

    public DapAn createDapAn(DapAn da) {
        if (da.getLaDapAnDung()) {
            // đảm bảo chỉ 1 đáp án đúng
            List<DapAn> list = dapAnRepo.findByMaCauHoi(da.getMaCauHoi());
            for (DapAn d : list) {
                d.setLaDapAnDung(false);
                dapAnRepo.save(d);
            }
        }
        return dapAnRepo.save(da);
    }

    public void deleteDapAn(String id) {
        dapAnRepo.deleteById(id);
    }
}
