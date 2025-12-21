package com.example.backend.security;

import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.service.RoleService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepo;
    private final RoleService roleService;

    public CustomUserDetailsService(NguoiDungRepository nguoiDungRepo,
                                    RoleService roleService) {
        this.nguoiDungRepo = nguoiDungRepo;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // ✅ KHÔNG dùng Optional, KHÔNG orElseThrow
        NguoiDung nd = nguoiDungRepo.findByTenDangNhap(username);

        if (nd == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roleService.getRolesByUserId(nd.getMaNguoiDung())) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new CustomUserDetails(
                nd.getTenDangNhap(),
                nd.getMatKhauMaHoa(),
                authorities
        );
    }
}
