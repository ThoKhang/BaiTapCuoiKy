package com.example.backend.service;

import com.example.backend.entity.MonHoc;
import com.example.backend.repository.MonHocRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMonHocService {

    private final MonHocRepository monHocRepo;

    public AdminMonHocService(MonHocRepository monHocRepo) {
        this.monHocRepo = monHocRepo;
    }

    // Lấy danh sách môn học
    public List<MonHoc> getAll() {
        return monHocRepo.findAll();
    }

    // Thêm môn học
    public MonHoc create(MonHoc monHoc) {
        if (monHocRepo.existsById(monHoc.getMaMonHoc())) {
            throw new RuntimeException("Mã môn học đã tồn tại");
        }
        if (monHocRepo.existsByTenMonHoc(monHoc.getTenMonHoc())) {
            throw new RuntimeException("Tên môn học đã tồn tại");
        }
        return monHocRepo.save(monHoc);
    }

    // Xoá môn học
    public void delete(String maMonHoc) {
        monHocRepo.deleteById(maMonHoc);
    }
}
