package com.example.backend.service;

import com.example.backend.model.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.service.IService.INguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguoiDungService implements INguoiDungService {

    @Autowired
    private NguoiDungRepository repo;

    @Override
    public List<NguoiDung> getAll() {
        return repo.findAll();
    }

    @Override
    public NguoiDung findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public NguoiDung createUser(NguoiDung user) {
        user.setMaNguoiDung(generateNewId());
        return repo.save(user);
    }

    @Override
    public String generateNewId() {
        long count = repo.count() + 1;
        return String.format("ND%03d", count);
    }
}
