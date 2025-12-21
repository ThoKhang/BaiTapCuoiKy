package com.example.backend.service;

import com.example.backend.entity.NguoiDungVaiTro;
import com.example.backend.entity.VaiTro;
import com.example.backend.repository.NguoiDungVaiTroRepository;
import com.example.backend.repository.VaiTroRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final NguoiDungVaiTroRepository nguoiDungVaiTroRepo;
    private final VaiTroRepository vaiTroRepo;

    public RoleService(NguoiDungVaiTroRepository nguoiDungVaiTroRepo,
                       VaiTroRepository vaiTroRepo) {
        this.nguoiDungVaiTroRepo = nguoiDungVaiTroRepo;
        this.vaiTroRepo = vaiTroRepo;
    }

    public List<String> getRolesByUserId(String maNguoiDung) {

        List<NguoiDungVaiTro> list = nguoiDungVaiTroRepo.findByMaNguoiDung(maNguoiDung);
        List<String> roles = new ArrayList<>();

        for (NguoiDungVaiTro ndvt : list) {

            Optional<VaiTro> vtOpt = vaiTroRepo.findById(ndvt.getMaVaiTro());

            if (vtOpt.isPresent()) {
                VaiTro vt = vtOpt.get();              // ✅ lấy ra entity
                roles.add("ROLE_" + vt.getMaVaiTro()); // ✅ ROLE_ADMIN / ROLE_USER
            }
        }
        return roles;
    }
}
