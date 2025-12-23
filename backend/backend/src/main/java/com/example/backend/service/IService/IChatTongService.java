package com.example.backend.service.IService;

import com.example.backend.dto.request.ChatTongSendRequest;
import com.example.backend.dto.response.ChatTongResponse;

import java.util.List;

public interface IChatTongService {
    ChatTongResponse send(ChatTongSendRequest request);

    List<ChatTongResponse> recent(Integer limit, Long beforeId);

    void recall(Long chatId, String maNguoiGui, String emailNguoiGui);
}
