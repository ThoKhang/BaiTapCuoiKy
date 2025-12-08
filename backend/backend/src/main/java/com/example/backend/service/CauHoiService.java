package com.example.backend.service;

import com.example.backend.dto.response.CauHoiResponse;
import com.example.backend.dto.response.DapAnResponse;
import com.example.backend.dto.response.DeCoBanResponse;
import com.example.backend.repository.CauHoiRepository;
import com.example.backend.service.IService.ICauHoiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CauHoiService implements ICauHoiService{

    @Autowired
    private EntityManager em;

    // ======================================================
    // 1) LẤY CÂU HỎI CỦA MỘT BÀI LÀM (kèm tất cả thông tin)
    // ======================================================
    public List<Map<String, Object>> getCauHoiByBaiLam(String maHoatDong) {
        String sql = """
            SELECT
                CH.MaCauHoi,
                CH.NoiDungCauHoi,
                CH.GiaiThich,
                CH.DiemToiDa,
                DAP.MaDapAn,
                DAP.NoiDungDapAn,
                DAP.LaDapAnDung,
                HQC.ThuTu
            FROM
                HoatDong_CauHoi HQC
            JOIN
                CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
            JOIN
                DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
            WHERE
                HQC.MaHoatDong = :maHoatDong
            ORDER BY
                HQC.ThuTu, DAP.MaDapAn
        """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("maHoatDong", maHoatDong)
                .getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] r : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("maCauHoi", (String) r[0]);
            map.put("noiDungCauHoi", (String) r[1]);
            map.put("giaiThich", (String) r[2]);
            map.put("diemToiDa", ((Number) r[3]).intValue());
            map.put("maDapAn", (String) r[4]);
            map.put("noiDungDapAn", (String) r[5]);
            map.put("laDapAnDung", (Boolean) r[6]);
            map.put("thuTu", ((Number) r[7]).intValue());

            result.add(map);
        }

        return result;
    }

    // ======================================================
    // 2) LẤY CÂU HỎI KHÔNG HIỂN THỊ ĐÁP ÁN ĐÚNG (dành cho học sinh)
    // ======================================================
    public List<Map<String, Object>> getCauHoiForLearning(String maHoatDong) {
        String sql = """
            SELECT
                CH.MaCauHoi,
                CH.NoiDungCauHoi,
                CH.DiemToiDa,
                DAP.MaDapAn,
                DAP.NoiDungDapAn,
                HQC.ThuTu
            FROM
                HoatDong_CauHoi HQC
            JOIN
                CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
            JOIN
                DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
            WHERE
                HQC.MaHoatDong = :maHoatDong
            ORDER BY
                HQC.ThuTu, DAP.MaDapAn
        """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("maHoatDong", maHoatDong)
                .getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] r : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("maCauHoi", (String) r[0]);
            map.put("noiDungCauHoi", (String) r[1]);
            map.put("diemToiDa", ((Number) r[2]).intValue());
            map.put("maDapAn", (String) r[3]);
            map.put("noiDungDapAn", (String) r[4]);
            map.put("thuTu", ((Number) r[5]).intValue());

            result.add(map);
        }

        return result;
    }

    // ======================================================
    // 3) HÀM HỖ TRỢ: XÁO TRỘN ĐÁP ÁN CỦA MỖI CÂU HỎI
    // ======================================================
    public List<Map<String, Object>> getCauHoiWithShuffledAnswers(String maHoatDong) {
        List<Map<String, Object>> allData = getCauHoiForLearning(maHoatDong);

        // Gom nhóm theo câu hỏi
        Map<String, Map<String, Object>> cauHoiMap = new LinkedHashMap<>();

        for (Map<String, Object> item : allData) {
            String maCauHoi = (String) item.get("maCauHoi");

            if (!cauHoiMap.containsKey(maCauHoi)) {
                Map<String, Object> cauHoiItem = new HashMap<>();
                cauHoiItem.put("maCauHoi", maCauHoi);
                cauHoiItem.put("noiDungCauHoi", item.get("noiDungCauHoi"));
                cauHoiItem.put("diemToiDa", item.get("diemToiDa"));
                cauHoiItem.put("thuTu", item.get("thuTu"));
                cauHoiItem.put("dapAn", new ArrayList<Map<String, Object>>());
                cauHoiMap.put(maCauHoi, cauHoiItem);
            }

            Map<String, Object> dapAn = new HashMap<>();
            dapAn.put("maDapAn", item.get("maDapAn"));
            dapAn.put("noiDungDapAn", item.get("noiDungDapAn"));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dapAnList =
                    (List<Map<String, Object>>) cauHoiMap.get(maCauHoi).get("dapAn");
            dapAnList.add(dapAn);
        }

        // Xáo trộn đáp án của mỗi câu hỏi
        List<Map<String, Object>> result = new ArrayList<>(cauHoiMap.values());
        for (Map<String, Object> cauHoi : result) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dapAnList =
                    (List<Map<String, Object>>) cauHoi.get("dapAn");
            Collections.shuffle(dapAnList);
        }

        return result;
    }

    // ======================================================
    // 4) HÀM SỬA DỮ LIỆU: FIX CÂU HỎI CĂNG CỐ
    // ======================================================
    @Transactional
    public Map<String, Object> fixCungCoDat() {
        try {
            // Xóa dữ liệu cũ
            em.createNativeQuery("DELETE FROM HoatDong_CauHoi WHERE MaHoatDong LIKE 'CC%'")
                    .executeUpdate();

            // Thêm lại Toán: CC001-CC010 -> CH001-CH100 (10 câu mỗi bài)
            int chToan = 1;
            for (int ccToan = 1; ccToan <= 10; ccToan++) {
                for (int thuTu = 1; thuTu <= 10; thuTu++) {
                    String maCungCo = String.format("CC%03d", ccToan);
                    String maCauHoi = String.format("CH%03d", chToan);

                    em.createNativeQuery(
                                    "INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu) VALUES (?, ?, ?)")
                            .setParameter(1, maCungCo)
                            .setParameter(2, maCauHoi)
                            .setParameter(3, thuTu)
                            .executeUpdate();

                    chToan++;
                }
            }

            // Thêm lại Tiếng Việt: CC011-CC020 -> CH156-CH200 (10 câu mỗi bài)
            int chTiengViet = 156;
            for (int ccTV = 11; ccTV <= 20; ccTV++) {
                for (int thuTu = 1; thuTu <= 10; thuTu++) {
                    String maCungCo = String.format("CC%03d", ccTV);
                    String maCauHoi = String.format("CH%03d", chTiengViet);

                    em.createNativeQuery(
                                    "INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu) VALUES (?, ?, ?)")
                            .setParameter(1, maCungCo)
                            .setParameter(2, maCauHoi)
                            .setParameter(3, thuTu)
                            .executeUpdate();

                    chTiengViet++;
                }
            }

            em.flush();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Đã sửa lại dữ liệu Căng cố thành công");
            response.put("detail", "CC001-CC010 (Toán): CH001-CH100, CC011-CC020 (Tiếng Việt): CH156-CH200");

            return response;

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Lỗi: " + e.getMessage());
            return error;
        }
    }

    // ======================================================
    // 5) HÀM KIỂM TRA: CHECK DỮ LIỆU CĂNG CỐ
    // ======================================================
    public java.util.List<Map<String, Object>> checkCungCoDat() {
        try {
            String sql = """
                SELECT 
                    HD.MaHoatDong,
                    MH.TenMonHoc,
                    COUNT(HQC.MaCauHoi) AS SoCau
                FROM HoatDong_CauHoi HQC
                JOIN HoatDongHocTap HD ON HQC.MaHoatDong = HD.MaHoatDong
                JOIN MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
                WHERE HD.MaLoai = 'LHD02'
                GROUP BY HD.MaHoatDong, MH.TenMonHoc
                ORDER BY HD.MaHoatDong
            """;

            @SuppressWarnings("unchecked")
            java.util.List<Object[]> results = em.createNativeQuery(sql).getResultList();

            java.util.List<Map<String, Object>> data = new java.util.ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("maHoatDong", (String) row[0]);
                map.put("tenMonHoc", (String) row[1]);
                map.put("soCau", ((Number) row[2]).intValue());
                data.add(map);
            }

            return data;

        } catch (Exception e) {
            java.util.List<Map<String, Object>> errorList = new java.util.ArrayList<>();
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Lỗi: " + e.getMessage());
            errorList.add(error);
            return errorList;
        }
    }
    //Start : decoban
    @Autowired
    private CauHoiRepository cauhoirepo;
    @Override
    public DeCoBanResponse getDeCoBan(int soDe) {
        DeCoBanResponse coBanResponse= new DeCoBanResponse();
        List<Object[]> listCoBan=cauhoirepo.deCoBan(soDe);
        if(listCoBan.isEmpty())
            return null;
        Map<String,CauHoiResponse> map = new LinkedHashMap<>();
        for(Object[] row : listCoBan){
            String maHD=(String) row[0];
            String tieuDe=(String) row[1];
            String MaCauHoi=(String) row[2];
            String NoiDungCauHoi=(String) row[3];
            int diemToiDa=(int)row[4];
            String MaDapAn=(String) row[5];
            String NoiDungDapAn=(String) row[6];
            boolean LaDapAnDung=(boolean) row[7];
            
            coBanResponse.setMaHoatDong(maHD);
            coBanResponse.setTieuDe(tieuDe);
            
            CauHoiResponse cauHoiRespons=map.get(MaCauHoi);
            if(cauHoiRespons==null){
                cauHoiRespons = new CauHoiResponse();
                cauHoiRespons.setMaCauHoi(MaCauHoi);
                cauHoiRespons.setNoiDungCauHoi(NoiDungCauHoi);
                cauHoiRespons.setDiemToiDa(diemToiDa);
                cauHoiRespons.setDapAn(new ArrayList<>());
                map.put(MaCauHoi, cauHoiRespons);
            }
            
            DapAnResponse DAResponse = new DapAnResponse();
            DAResponse.setLaDapAnDung(LaDapAnDung);
            DAResponse.setMaCauHoi(MaCauHoi);
            DAResponse.setMaDapAn(MaDapAn);
            DAResponse.setNoiDungDapAn(NoiDungDapAn);
            cauHoiRespons.getDapAn().add(DAResponse);
            
        }
        coBanResponse.setDanhSachCauHoi(new ArrayList<>(map.values()));
        return coBanResponse;
    }
    //End : decoban
}