package com.example.backend.controller;

import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.security.JwtUtil;
import com.example.backend.service.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final NguoiDungRepository nguoiDungRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(NguoiDungRepository nguoiDungRepo,
                          RoleService roleService,
                          PasswordEncoder passwordEncoder) {
        this.nguoiDungRepo = nguoiDungRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {

        String username = body.get("tenDangNhap");
        String password = body.get("matKhau");

        NguoiDung nd = nguoiDungRepo.findByTenDangNhap(username);
        if (nd == null || !passwordEncoder.matches(password, nd.getMatKhauMaHoa())) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
        }

        List<String> roles = roleService.getRolesByUserId(nd.getMaNguoiDung());
        String token = JwtUtil.generateToken(username, roles);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("roles", roles);

        return result;
    }
}
