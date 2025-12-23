package com.example.backend.converter;

import com.example.backend.dto.response.ChatTongResponse;
import com.example.backend.entity.ChatTong;

public class ChatTongConverter {

    public static ChatTongResponse toResponse(ChatTong e) {
        ChatTongResponse dto = new ChatTongResponse();
        dto.setId(e.getId());

        if (e.getNguoiGui() != null) {
            dto.setMaNguoiGui(e.getNguoiGui().getMaNguoiDung());
            dto.setTenDangNhapNguoiGui(e.getNguoiGui().getTenDangNhap());
        }

        dto.setDaThuHoi(Boolean.TRUE.equals(e.getDaThuHoi()));
        dto.setNgayGui(e.getNgayGui());

        // Nếu thu hồi: frontend có thể hiện "Tin nhắn đã thu hồi"
        dto.setNoiDung(Boolean.TRUE.equals(e.getDaThuHoi()) ? "" : e.getNoiDung());

        if (e.getTraLoiTinNhan() != null) {
            dto.setIdTraLoi(e.getTraLoiTinNhan().getId());
            String nd = e.getTraLoiTinNhan().getNoiDung();
            if (nd != null) {
                String preview = nd.length() > 60 ? nd.substring(0, 60) + "..." : nd;
                dto.setTraLoiPreview(preview);
            }
        }

        return dto;
    }
}
