package com.example.backend.service.IService;

import com.example.backend.dto.request.HoanThanhLyThuyetRequest;
import com.example.backend.dto.response.LyThuyetDetailResponse;
import com.example.backend.dto.response.LyThuyetItemResponse;
import java.util.List;

public interface ILyThuyetService {
    List<LyThuyetItemResponse> layDanhSachTheoMon(String email, String maMonHoc);
    String hoanThanh(HoanThanhLyThuyetRequest request);
    LyThuyetDetailResponse layChiTiet(String email, String maHoatDong);
}
