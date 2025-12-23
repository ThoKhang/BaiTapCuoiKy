package com.example.backend.controller;

import com.example.backend.dto.request.ChatTongRecallRequest;
import com.example.backend.dto.request.ChatTongSendRequest;
import com.example.backend.dto.response.ChatTongResponse;
import com.example.backend.service.IService.IChatTongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-tong")
@CrossOrigin("*")
public class ChatTongController {

    @Autowired
    private IChatTongService chatTongService;

    // Gửi tin nhắn chat tổng
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody ChatTongSendRequest request) {
        try {
            ChatTongResponse res = chatTongService.send(request);
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Lấy danh sách tin nhắn gần nhất (limit) - hỗ trợ beforeId để cuộn lên lấy thêm
    // Ví dụ:
    // GET /api/chat-tong/recent?limit=50
    // GET /api/chat-tong/recent?limit=50&beforeId=1200
    @GetMapping("/recent")
    public ResponseEntity<List<ChatTongResponse>> recent(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Long beforeId
    ) {
        return ResponseEntity.ok(chatTongService.recent(limit, beforeId));
    }

    // Thu hồi tin nhắn (chỉ chủ tin nhắn được thu hồi)
    @PutMapping("/{id}/recall")
    public ResponseEntity<Void> recall(@PathVariable Long id,
                                       @RequestBody ChatTongRecallRequest body) {
        chatTongService.recall(id, body.getMaNguoiGui(), body.getEmailNguoiGui());
        return ResponseEntity.ok().build(); // 🔥 KHÔNG TRẢ STRING
    }

}
