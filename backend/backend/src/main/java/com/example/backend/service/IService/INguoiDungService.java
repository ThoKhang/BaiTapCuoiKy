package com.example.backend.service.IService;

import com.example.backend.model.NguoiDung;
import java.util.List;

public interface INguoiDungService {
    List<NguoiDung> getAll();
    NguoiDung findByEmail(String email);
    NguoiDung createUser(NguoiDung user);
    String generateNewId();  // Tạo mã ND001
}
