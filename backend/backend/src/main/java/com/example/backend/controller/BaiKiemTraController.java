package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/baikiemtra")
@CrossOrigin(origins = "*")
public class BaiKiemTraController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String loadSql(String fileName) throws Exception {
        var resource = new ClassPathResource("sql/" + fileName);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    /**
     * ✅ API: Lấy danh sách bài củng cố theo môn học + người dùng
     * URL ví dụ: /api/baikiemtra/cungco/1/1
     *  - maMonHoc = 1 (Toán)
     *  - maNguoiDung = 1 (User hiện tại)
     */
    @GetMapping("/cungco/{maMonHoc}/{maNguoiDung}")
    public ResponseEntity<?> getDanhSachBaiKiemTraCungCoTheoMon(
            @PathVariable int maMonHoc,
            @PathVariable int maNguoiDung) {

        try {
            // Gọi thủ tục trong SQL Server có 2 tham số
            String sql = """
                    EXEC dbo.sp_LayBaiKiemTraCungCoTheoMon @MaMonHoc = ?, @MaNguoiDung = ?
                    """;

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, maMonHoc, maNguoiDung);

            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Lỗi khi lấy danh sách bài kiểm tra củng cố: " + e.getMessage());
        }
    }
}
