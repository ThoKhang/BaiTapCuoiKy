package com.example.backend.service;

import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNguoiDungService {

    private final NguoiDungRepository nguoiDungRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminNguoiDungService(NguoiDungRepository nguoiDungRepo,
                                 PasswordEncoder passwordEncoder) {
        this.nguoiDungRepo = nguoiDungRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<NguoiDung> getAll() {
        return nguoiDungRepo.findAll();
    }

    public NguoiDung getById(String id) {
        return nguoiDungRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    public NguoiDung create(NguoiDung nd) {
        if (nguoiDungRepo.existsById(nd.getMaNguoiDung())) {
            throw new RuntimeException("Mã người dùng đã tồn tại");
        }

        // hash password
        nd.setMatKhauMaHoa(passwordEncoder.encode(nd.getMatKhauMaHoa()));
        return nguoiDungRepo.save(nd);
    }

    public NguoiDung update(String id, NguoiDung nd) {
        NguoiDung old = getById(id);
        old.setEmail(nd.getEmail());
        old.setTongDiem(nd.getTongDiem());
        return nguoiDungRepo.save(old);
    }

    public void delete(String id) {
        nguoiDungRepo.deleteById(id);
    }

    // reset mật khẩu
    public void resetPassword(String id, String newPassword) {
        NguoiDung nd = getById(id);
        nd.setMatKhauMaHoa(passwordEncoder.encode(newPassword));
        nguoiDungRepo.save(nd);
    }
}
