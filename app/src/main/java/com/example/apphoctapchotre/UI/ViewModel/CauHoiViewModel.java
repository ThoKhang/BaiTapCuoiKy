package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.CauHoiRepository;
import com.example.apphoctapchotre.DATA.model.CauHoi;
import com.example.apphoctapchotre.DATA.model.DapAn;

import java.util.List;

public class CauHoiViewModel extends ViewModel {

    private final CauHoiRepository repository = new CauHoiRepository();

    private LiveData<List<CauHoi>> danhSachCauHoi;
    private LiveData<CauHoi> cauHoiHienTai;

    private MutableLiveData<Integer> vitriCauHoi = new MutableLiveData<>(0);
    private MutableLiveData<String> dapAnDaChon = new MutableLiveData<>();
    private MutableLiveData<Integer> diemDatDuoc = new MutableLiveData<>(0);

    // =======================================================
    // 1) LẤY DANH SÁCH CÂU HỎI
    // =======================================================
    public void loadDanhSachCauHoi(String maHoatDong) {
        danhSachCauHoi = repository.getDanhSachCauHoi(maHoatDong);
    }

    public LiveData<List<CauHoi>> getDanhSachCauHoi() {
        return danhSachCauHoi;
    }

    // =======================================================
    // 2) LẤY CÂU HỎI HIỆN TẠI
    // =======================================================
    public CauHoi getCauHoiHienTai(List<CauHoi> danhSach) {
        int viTri = vitriCauHoi.getValue() != null ? vitriCauHoi.getValue() : 0;
        if (danhSach != null && viTri < danhSach.size()) {
            return danhSach.get(viTri);
        }
        return null;
    }

    // =======================================================
    // 3) CHUYỂN ĐẾN CÂU HỎI TIẾP THEO
    // =======================================================
    public void nextQuestion(List<CauHoi> danhSach) {
        int viTri = vitriCauHoi.getValue() != null ? vitriCauHoi.getValue() : 0;
        if (viTri < danhSach.size() - 1) {
            vitriCauHoi.setValue(viTri + 1);
            dapAnDaChon.setValue(""); // Reset đáp án đã chọn
        }
    }

    // =======================================================
    // 4) QUAY LẠI CÂU HỎI TRƯỚC ĐÓ
    // =======================================================
    public void previousQuestion() {
        int viTri = vitriCauHoi.getValue() != null ? vitriCauHoi.getValue() : 0;
        if (viTri > 0) {
            vitriCauHoi.setValue(viTri - 1);
            dapAnDaChon.setValue(""); // Reset đáp án đã chọn
        }
    }

    // =======================================================
    // 5) CHỌN ĐÁP ÁN
    // =======================================================
    public void chonDapAn(String maDapAn) {
        dapAnDaChon.setValue(maDapAn);
    }

    public LiveData<String> getDapAnDaChon() {
        return dapAnDaChon;
    }

    // =======================================================
    // 6) KIỂM TRA ĐÁP ÁN ĐÚNG
    // =======================================================
    public boolean kiemTraDapAnDung(CauHoi cauHoi, String maDapAnChon) {
        if (cauHoi != null && cauHoi.getDapAn() != null) {
            // Giả sử đáp án đầu tiên trong danh sách là đáp án đúng
            // Bạn cần update API để trả về field "isDung" hoặc tương tự
            DapAn dapAnDung = cauHoi.getDapAn().get(0);
            if (dapAnDung != null) {
                return dapAnDung.getMaDapAn().equals(maDapAnChon);
            }
        }
        return false;
    }

    // =======================================================
    // 7) CẬP NHẬT ĐIỂM
    // =======================================================
    public void congDiem(int diem) {
        int diemHienTai = diemDatDuoc.getValue() != null ? diemDatDuoc.getValue() : 0;
        diemDatDuoc.setValue(diemHienTai + diem);
    }

    public LiveData<Integer> getDiemDatDuoc() {
        return diemDatDuoc;
    }

    // =======================================================
    // 8) GET/SET VỊ TRÍ CÂU HỎI
    // =======================================================
    public LiveData<Integer> getVitriCauHoi() {
        return vitriCauHoi;
    }

    public void setVitriCauHoi(int viTri) {
        vitriCauHoi.setValue(viTri);
    }

    public int getTongCauHoi(List<CauHoi> danhSach) {
        return danhSach != null ? danhSach.size() : 0;
    }
    public boolean isCorrectAnswer(CauHoi cauHoi, String maDapAnChon) {
        for (DapAn da : cauHoi.getDapAn()) {
            if (da.getMaDapAn().equals(maDapAnChon)) {
                return da.isLaDapAnDung();  // trả về đúng hay sai
            }
        }
        return false;
    }

}