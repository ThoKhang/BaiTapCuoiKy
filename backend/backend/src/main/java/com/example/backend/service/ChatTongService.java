package com.example.backend.service;

import com.example.backend.converter.ChatTongConverter;
import com.example.backend.dto.request.ChatTongSendRequest;
import com.example.backend.dto.response.ChatTongResponse;
import com.example.backend.entity.ChatTong;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.ChatTongRepository;
import com.example.backend.repository.NguoiDungRepository;
import com.example.backend.service.IService.IChatTongService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatTongService implements IChatTongService {

    @Autowired
    private ChatTongRepository chatTongRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    private NguoiDung resolveSender(String maNguoiGui, String emailNguoiGui) {
        NguoiDung nd = null;

        if (emailNguoiGui != null && !emailNguoiGui.isBlank()) {
            nd = nguoiDungRepository.findByEmail(emailNguoiGui.trim());
        } else if (maNguoiGui != null && !maNguoiGui.isBlank()) {
            // JpaRepository<NguoiDung, String> => findById
            nd = nguoiDungRepository.findById(maNguoiGui.trim()).orElse(null);
        }

        if (nd == null) {
            throw new RuntimeException("Không tìm thấy người gửi (maNguoiGui/email không hợp lệ).");
        }
        return nd;
    }

    @Override
    public ChatTongResponse send(ChatTongSendRequest request) {
        if (request == null) throw new RuntimeException("Request is null.");
        if (request.getNoiDung() == null || request.getNoiDung().trim().isEmpty()) {
            throw new RuntimeException("Nội dung không được để trống.");
        }

        NguoiDung sender = resolveSender(request.getMaNguoiGui(), request.getEmailNguoiGui());

        ChatTong chat = new ChatTong();
        chat.setNguoiGui(sender);
        chat.setNoiDung(request.getNoiDung().trim());
        chat.setDaThuHoi(false);

        if (request.getIdTraLoi() != null) {
            ChatTong replyTo = chatTongRepository.findById(request.getIdTraLoi()).orElse(null);
            if (replyTo == null) throw new RuntimeException("Tin nhắn trả lời không tồn tại.");
            chat.setTraLoiTinNhan(replyTo);
        }

        ChatTong saved = chatTongRepository.save(chat);
        return ChatTongConverter.toResponse(saved);
    }

    @Override
    public List<ChatTongResponse> recent(Integer limit, Long beforeId) {
        int l = (limit == null || limit <= 0) ? 50 : Math.min(limit, 200);

        List<ChatTong> list;
        if (beforeId != null && beforeId > 0) {
            list = chatTongRepository.findLatestBeforeId(beforeId, PageRequest.of(0, l));
        } else {
            list = chatTongRepository.findLatest(PageRequest.of(0, l));
        }

        // trả về DESC như query, frontend muốn ASC thì đảo lại ở FE hoặc đảo ở đây
        List<ChatTongResponse> res = list.stream()
                .map(ChatTongConverter::toResponse)
                .collect(Collectors.toList());

        Collections.reverse(res); //  QUAN TRỌNG

        return res;

    }

    @Override
    public void recall(Long chatId, String maNguoiGui, String emailNguoiGui) {
        if (chatId == null || chatId <= 0) throw new RuntimeException("chatId không hợp lệ.");

        NguoiDung sender = resolveSender(maNguoiGui, emailNguoiGui);

        ChatTong chat = chatTongRepository.findById(chatId).orElse(null);
        if (chat == null) throw new RuntimeException("Không tìm thấy tin nhắn.");

        // Chỉ cho phép thu hồi tin của chính mình
        if (chat.getNguoiGui() == null || !sender.getMaNguoiDung().equals(chat.getNguoiGui().getMaNguoiDung())) {
            throw new RuntimeException("Bạn không có quyền thu hồi tin nhắn này.");
        }

        if (Boolean.TRUE.equals(chat.getDaThuHoi())) return;

        chat.setDaThuHoi(true);

        // Option: giữ nguyên nội dung trong DB, hoặc xóa trắng để an toàn
        chat.setNoiDung("");

        chatTongRepository.save(chat);
    }
}
