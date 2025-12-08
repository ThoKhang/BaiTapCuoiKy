package com.example.backend.controller;

import com.example.backend.dto.response.DeOnLuyenResponse;
import com.example.backend.service.CauHoiService;
import com.example.backend.service.IService.ICauHoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cauhoi")
@CrossOrigin("*")
public class CauHoiController {

    @Autowired
    private CauHoiService service;

    // ====================================
    // 1) API Lấy tất cả câu hỏi của bài làm
    // ====================================
    @GetMapping("/bailam/{maHoatDong}")
    public ResponseEntity<?> getCauHoiByBaiLam(@PathVariable String maHoatDong) {
        return ResponseEntity.ok(service.getCauHoiByBaiLam(maHoatDong));
    }

    // ====================================
    // 2) API Lấy câu hỏi để học sinh làm (không hiển thị đáp án đúng)
    // ====================================
    @GetMapping("/hoitap/{maHoatDong}")
    public ResponseEntity<?> getCauHoiForLearning(@PathVariable String maHoatDong) {
        return ResponseEntity.ok(service.getCauHoiForLearning(maHoatDong));
    }

    // ====================================
    // 3) API Lấy câu hỏi với đáp án xáo trộn
    // ====================================
    @GetMapping("/hoitap-xaotro/{maHoatDong}")
    public ResponseEntity<?> getCauHoiWithShuffledAnswers(@PathVariable String maHoatDong) {
        return ResponseEntity.ok(service.getCauHoiWithShuffledAnswers(maHoatDong));
    }

    // ====================================
    // 4) API Sửa lại dữ liệu Căng cố
    // ====================================
    @PostMapping("/fix-cung-co")
    public ResponseEntity<?> fixCungCoDat() {
        return ResponseEntity.ok(service.fixCungCoDat());
    }

    // ====================================
    // 5) API Kiểm tra dữ liệu Căng cố
    // ====================================
    @GetMapping("/check-cung-co")
    public ResponseEntity<?> checkCungCoDat() {
        return ResponseEntity.ok(service.checkCungCoDat());
    }
    
    //Start : decoban
    @Autowired
    private ICauHoiService cauhoi;
    @GetMapping("/onluyen-coban")
    public ResponseEntity<?> deCoBan(@RequestParam("tieuDe") String tieuDe){
        DeOnLuyenResponse coBanResponse = cauhoi.getDeCoBan(tieuDe);
        if(coBanResponse==null)
            return ResponseEntity.badRequest().body("Khôn tìm thấy đề : "+tieuDe);
        return ResponseEntity.ok(coBanResponse);
    }
    //End : decoban
}