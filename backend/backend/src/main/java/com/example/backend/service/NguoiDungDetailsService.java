package com.example.backend.service;

import com.example.backend.config.NguoiDungDetails;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.NguoiDungRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NguoiDungDetailsService implements UserDetailsService {

    private final NguoiDungRepository repo;

    public NguoiDungDetailsService(NguoiDungRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NguoiDung nd = repo.findByEmail(email);

        if (nd == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        }

        return new NguoiDungDetails(nd);
    }
}
