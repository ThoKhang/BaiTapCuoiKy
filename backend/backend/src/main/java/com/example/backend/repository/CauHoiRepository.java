package com.example.backend.repository;

import com.example.backend.entity.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CauHoiRepository extends JpaRepository<CauHoi, String> {

    // Lấy tất cả câu hỏi của một bài làm cụ thể (kèm đáp án)
    @Query(value = """
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
    """, nativeQuery = true)
    List<Map<String, Object>> getCauHoiByHoatDong(@Param("maHoatDong") String maHoatDong);

    // Lấy câu hỏi kèm đáp án (không hiển thị đáp án đúng cho người học)
    @Query(value = """
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
    """, nativeQuery = true)
    List<Map<String, Object>> getCauHoiForLearning(@Param("maHoatDong") String maHoatDong);
    
    //Start : Ôn luyện
    @Query(value = "Select h.MaHoatDong,h.TieuDe,c.MaCauHoi,c.NoiDungCauHoi,c.DiemToiDa,d.MaDapAn,d.NoiDungDapAn,d.LaDapAnDung,c.GiaiThich from HoatDongHocTap h "
            + "join HoatDong_CauHoi hc on h.MaHoatDong = hc.MaHoatDong "
            + "join CauHoi c on c.MaCauHoi=hc.MaCauHoi "
            + "join DapAn d on d.MaCauHoi=c.MaCauHoi "
            + "where h.TieuDe=:tieuDe "
            + "order by c.MaCauHoi, d.MaDapAn ",nativeQuery = true)
    List<Object[]> deCoBan(@Param("tieuDe") String tieuDe);
    //End : Ôn luyện

}