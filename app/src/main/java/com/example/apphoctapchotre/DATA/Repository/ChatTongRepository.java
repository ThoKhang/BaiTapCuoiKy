package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.ChatTongRecallRequest;
import com.example.apphoctapchotre.DATA.model.ChatTongResponse;
import com.example.apphoctapchotre.DATA.model.ChatTongSendRequest;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class ChatTongRepository {

    private final ApiService apiService;

    public ChatTongRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    // ===== LOAD CHAT =====
    public Call<List<ChatTongResponse>> getRecentMessages(Integer limit, Long beforeId) {
        return apiService.getChatTongRecent(limit, beforeId);
    }

    // ===== GỬI TIN =====
    public Call<ChatTongResponse> sendMessage(ChatTongSendRequest request) {
        return apiService.sendChatTong(request);
    }

    // =====  GỠ TIN NHẮN (FIX Ở ĐÂY) =====
    public Call<Void> recallMessage(Long chatId, String maNguoiDung) {
        ChatTongRecallRequest body = new ChatTongRecallRequest();
        body.setMaNguoiGui(maNguoiDung);
        body.setEmailNguoiGui(null);
        return apiService.recallChatTong(chatId, body);
    }

}
