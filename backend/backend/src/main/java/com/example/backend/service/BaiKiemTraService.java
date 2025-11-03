package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaiKiemTraService {

    @Autowired
    private BaiKiemTraRepository baiKiemTraRepository;

    @Autowired
    private CauHoiRepository cauHoiRepository;

    @Autowired
    private LuaChonRepository luaChonRepository;

    @Autowired
    private BaiKiemTraCauHoiRepository baiKiemTraCauHoiRepository;

    @Autowired
    private LanThuBaiKiemTraRepository lanThuBaiKiemTraRepository;

    @Autowired
    private DapAnRepository dapAnRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public List<BaiKiemTra> getBaiKiemTraCungCoByMonHoc(Byte maMonHoc) {
        return baiKiemTraRepository.findByLoaiBaiKiemTraAndMaMonHoc("CungCo", maMonHoc);
    }

    public List<Map<String, Object>> getCauHoiForBaiKiemTra(Integer maBaiKiemTra, boolean shuffle) {
        BaiKiemTra bai = baiKiemTraRepository.findById(maBaiKiemTra).orElseThrow(() -> new RuntimeException("Bài kiểm tra không tồn tại"));
        List<BaiKiemTraCauHoi> links = baiKiemTraCauHoiRepository.findByKeyMaBaiKiemTra(maBaiKiemTra);
        List<Integer> cauHoiIds = links.stream()
                .sorted(Comparator.comparing(BaiKiemTraCauHoi::getThuTu))
                .map(b -> b.getKey().getMaCauHoi())
                .collect(Collectors.toList());

        if (shuffle && bai.getCoXaoTron()) {
            Collections.shuffle(cauHoiIds);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Integer id : cauHoiIds) {
            CauHoi cau = cauHoiRepository.findById(id).orElseThrow(() -> new RuntimeException("Câu hỏi không tồn tại"));
            List<LuaChon> luaChons = luaChonRepository.findByMaCauHoi(id);
            Map<String, Object> map = new HashMap<>();
            map.put("cauHoi", cau);
            map.put("luaChon", luaChons);
            result.add(map);
        }
        return result;
    }

    @Transactional
    public LanThuBaiKiemTraNguoiDung submitBaiKiemTra(Integer maNguoiDung, Integer maBaiKiemTra, Map<Integer, Integer> dapAnMap) {
        BaiKiemTra bai = baiKiemTraRepository.findById(maBaiKiemTra).orElseThrow(() -> new RuntimeException("Bài kiểm tra không tồn tại"));
        int diemToiDa = bai.getTongSoCauHoi() * 10;
        int diem = 0;

        LanThuBaiKiemTraNguoiDung lanThu = new LanThuBaiKiemTraNguoiDung();
        lanThu.setMaNguoiDung(maNguoiDung);
        lanThu.setMaBaiKiemTra(maBaiKiemTra);
        lanThu.setDiemToiDa(diemToiDa);
        lanThu = lanThuBaiKiemTraRepository.save(lanThu);

        for (Map.Entry<Integer, Integer> entry : dapAnMap.entrySet()) {
            Integer maCauHoi = entry.getKey();
            Integer maLuaChonChon = entry.getValue();
            LuaChon luaChon = luaChonRepository.findById(maLuaChonChon).orElseThrow(() -> new RuntimeException("Lựa chọn không tồn tại"));
            boolean laDung = luaChon.getLaDung();

            if (laDung) {
                diem += 10;
            }

            DapAnKey key = new DapAnKey();
            key.setMaLanThu(lanThu.getMaLanThu());
            key.setMaCauHoi(maCauHoi);
            DapAnCauHoiNguoiDung dapAn = new DapAnCauHoiNguoiDung();
            dapAn.setKey(key);
            dapAn.setMaLuaChonChon(maLuaChonChon);
            dapAn.setLaDung(laDung);
            dapAnRepository.save(dapAn);
        }

        lanThu.setDiem(diem);
        lanThuBaiKiemTraRepository.save(lanThu);

        NguoiDung nguoiDung = nguoiDungRepository.findById(maNguoiDung).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        nguoiDung.setTongDiem(nguoiDung.getTongDiem() + diem);
        nguoiDungRepository.save(nguoiDung);

        return lanThu;
    }
}