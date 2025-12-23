package com.example.backend.service;

import com.example.backend.dto.request.MediaProgressRequest;
import com.example.backend.dto.response.MediaProgressResponse;
import com.example.backend.entity.Media;
import com.example.backend.entity.MediaNguoiDung;
import com.example.backend.entity.MediaNguoiDungId;
import com.example.backend.entity.NguoiDung;
import com.example.backend.repository.MediaNguoiDungRepository;
import com.example.backend.repository.MediaRepository;
import com.example.backend.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class MediaService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    private final MediaRepository mediaRepository;
    private final MediaNguoiDungRepository mediaNguoiDungRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public MediaService(MediaRepository mediaRepository,
                        MediaNguoiDungRepository mediaNguoiDungRepository,
                        NguoiDungRepository nguoiDungRepository) {
        this.mediaRepository = mediaRepository;
        this.mediaNguoiDungRepository = mediaNguoiDungRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    public Media upload(MultipartFile file, String loaiMedia, String tieuDe, String moTa, Integer thoiLuongGiay) {
        if (file == null || file.isEmpty()) throw new RuntimeException("File upload rỗng!");
        if (!"VIDEO".equalsIgnoreCase(loaiMedia) && !"AUDIO".equalsIgnoreCase(loaiMedia)) {
            throw new RuntimeException("LoaiMedia chỉ được VIDEO hoặc AUDIO!");
        }

        String folder = "VIDEO".equalsIgnoreCase(loaiMedia) ? "videos" : "audios";
        Path dirPath = Paths.get(uploadDir, folder).toAbsolutePath().normalize();

        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("Không tạo được thư mục uploads!");
        }

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String ext = "";
        int idx = original.lastIndexOf('.');
        if (idx >= 0) ext = original.substring(idx);

        String savedName = UUID.randomUUID() + ext;
        Path target = dirPath.resolve(savedName);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Lưu file thất bại!");
        }

        // lưu đường dẫn tương đối để dễ deploy
        String relativePath = uploadDir + "/" + folder + "/" + savedName;

        Media media = new Media();
        media.setLoaiMedia(loaiMedia.toUpperCase());
        media.setTieuDe(tieuDe);
        media.setMoTa(moTa);
        media.setThoiLuongGiay(thoiLuongGiay);
        media.setDuongDanFile(relativePath);

        return mediaRepository.save(media);
    }

    public MediaProgressResponse getProgress(Long maMedia, String email) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email);
        if (nd == null) throw new RuntimeException("Không tìm thấy người dùng!");

        MediaNguoiDung mnd = mediaNguoiDungRepository
                .findById_MaMediaAndId_MaNguoiDung(maMedia, nd.getMaNguoiDung())
                .orElse(null);

        MediaProgressResponse res = new MediaProgressResponse();
        res.setMaMedia(maMedia);
        res.setMaNguoiDung(nd.getMaNguoiDung());

        if (mnd == null) {
            res.setDaXem(false);
            res.setViTriGiay(0);
            res.setLanXemCuoi(null);
            return res;
        }

        res.setDaXem(mnd.isDaXem());
        res.setViTriGiay(mnd.getViTriGiay());
        res.setLanXemCuoi(mnd.getLanXemCuoi());
        return res;
    }

    public MediaProgressResponse saveProgress(Long maMedia, String email, MediaProgressRequest req) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email);
        if (nd == null) throw new RuntimeException("Không tìm thấy người dùng!");

        Media media = mediaRepository.findById(maMedia)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy media!"));

        MediaNguoiDung mnd = mediaNguoiDungRepository
                .findById_MaMediaAndId_MaNguoiDung(maMedia, nd.getMaNguoiDung())
                .orElseGet(() -> {
                    MediaNguoiDung x = new MediaNguoiDung();
                    x.setId(new MediaNguoiDungId(maMedia, nd.getMaNguoiDung()));
                    x.setMedia(media);
                    x.setNguoiDung(nd);
                    x.setDaXem(false);
                    x.setViTriGiay(0);
                    return x;
                });

        mnd.setViTriGiay(Math.max(0, req.getViTriGiay()));
        mnd.setDaXem(req.isDaXem());
        mediaNguoiDungRepository.save(mnd);

        MediaProgressResponse res = new MediaProgressResponse();
        res.setMaMedia(maMedia);
        res.setMaNguoiDung(nd.getMaNguoiDung());
        res.setDaXem(mnd.isDaXem());
        res.setViTriGiay(mnd.getViTriGiay());
        res.setLanXemCuoi(mnd.getLanXemCuoi());
        return res;
    }
}
