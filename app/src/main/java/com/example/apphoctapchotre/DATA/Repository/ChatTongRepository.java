package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.*;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class ChatTongRepository {

    private final ApiService apiService;

    public ChatTongRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public Call<List<ChatTongResponse>> getRecentMessages(Integer limit, Long beforeId) {
        return apiService.getChatTongRecent(limit, beforeId);
    }

    public Call<ChatTongResponse> sendMessage(ChatTongSendRequest request) {
        return apiService.sendChatTong(request);
    }

    public Call<String> recallMessage(Long id, ChatTongRecallRequest request) {
        return apiService.recallChatTong(id, request);
    }
}
