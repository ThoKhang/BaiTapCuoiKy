package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cauhoi")
@CrossOrigin(origins = "*")
public class CauHoiController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<?> getCauHoiTheoTieuDePhu(
            @RequestParam int maMonHoc,
            @RequestParam String tieuDePhu) {
        try {
            String sql = "EXEC dbo.sp_LayCauHoiTheoTieuDePhu @MaMonHoc = ?, @TieuDePhu = ?";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, maMonHoc, tieuDePhu);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Lỗi khi lấy danh sách câu hỏi: " + e.getMessage());
        }
    }
}
