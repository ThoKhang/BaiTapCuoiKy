package com.example.backend.controller;

import com.example.backend.model.BaiKiemTra;
import com.example.backend.model.LanThuBaiKiemTraNguoiDung;
import com.example.backend.service.BaiKiemTraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/baikiemtra")
public class BaiKiemTraController {

    @Autowired
    private BaiKiemTraService baiKiemTraService;

    @GetMapping("/cungco/{maMonHoc}")
    public ResponseEntity<List<BaiKiemTra>> getBaiKiemTraCungCoByMonHoc(@PathVariable Byte maMonHoc) {
        List<BaiKiemTra> list = baiKiemTraService.getBaiKiemTraCungCoByMonHoc(maMonHoc);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/cauhoi/{maBaiKiemTra}")
    public ResponseEntity<List<Map<String, Object>>> getCauHoiForBaiKiemTra(
            @PathVariable Integer maBaiKiemTra,
            @RequestParam(defaultValue = "true") boolean shuffle) {
        List<Map<String, Object>> cauHoiList = baiKiemTraService.getCauHoiForBaiKiemTra(maBaiKiemTra, shuffle);
        return ResponseEntity.ok(cauHoiList);
    }

    @PostMapping("/submit")
    public ResponseEntity<LanThuBaiKiemTraNguoiDung> submitBaiKiemTra(
            @RequestParam Integer maNguoiDung,
            @RequestParam Integer maBaiKiemTra,
            @RequestBody Map<Integer, Integer> dapAnMap) {
        LanThuBaiKiemTraNguoiDung ketQua = baiKiemTraService.submitBaiKiemTra(maNguoiDung, maBaiKiemTra, dapAnMap);
        return ResponseEntity.ok(ketQua);
    }
}