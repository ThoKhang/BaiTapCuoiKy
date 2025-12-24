package com.example.backend.controller;

import com.example.backend.dto.request.MediaProgressRequest;
import com.example.backend.dto.response.MediaProgressResponse;
import com.example.backend.entity.Media;
import com.example.backend.repository.MediaRepository;
import com.example.backend.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@CrossOrigin("*")
public class MediaController {

    private final MediaService mediaService;
    private final MediaRepository mediaRepository;

    public MediaController(MediaService mediaService, MediaRepository mediaRepository) {
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    // List media theo loại: VIDEO hoặc AUDIO
    @GetMapping
    public ResponseEntity<List<Media>> list(@RequestParam String loaiMedia) {
        return ResponseEntity.ok(mediaRepository.findByLoaiMediaOrderByMaMediaDesc(loaiMedia.toUpperCase()));
    }

    // Upload media (admin hoặc bạn test)
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("loaiMedia") String loaiMedia,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam(value = "moTa", required = false) String moTa,
            @RequestParam(value = "thoiLuongGiay", required = false) Integer thoiLuongGiay
    ) {
        Media saved = mediaService.upload(file, loaiMedia, tieuDe, moTa, thoiLuongGiay);
        return ResponseEntity.ok(saved);
    }

    // Lấy tiến trình theo user (tạm dùng email; sau này đổi sang JWT principal)
    @GetMapping("/{maMedia}/progress")
    public ResponseEntity<MediaProgressResponse> getProgress(
            @PathVariable Long maMedia,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(mediaService.getProgress(maMedia, email));
    }

    // Lưu tiến trình
    @PutMapping("/{maMedia}/progress")
    public ResponseEntity<MediaProgressResponse> saveProgress(
            @PathVariable Long maMedia,
            @RequestParam String email,
            @RequestBody MediaProgressRequest req
    ) {
        return ResponseEntity.ok(mediaService.saveProgress(maMedia, email, req));
    }
}
