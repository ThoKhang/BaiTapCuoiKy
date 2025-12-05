package com.example.apphoctapchotre.UI.ViewModel;
import androidx.lifecycle.ViewModel;
import com.example.apphoctapchotre.DATA.Repository.CungCoRepository;
import com.example.apphoctapchotre.DATA.model.CungCoMonHocResponse;
import com.example.apphoctapchotre.DATA.model.CungCoDaLamResponse;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CungCoViewModel2 extends ViewModel {

    private final CungCoRepository repository = new CungCoRepository();
    private LiveData<List<CungCoMonHocResponse>> danhSach;
    private LiveData<List<CungCoDaLamResponse>> danhSachDaLam;
    private String maNguoiDung;

    // ====================================================
    // 1) LẤY BÀI CHƯA LÀM
    // ====================================================
    public void loadDanhSach(String maMonHoc) {
        danhSach = repository.getDanhSachCungCo(maMonHoc);
    }

    public LiveData<List<CungCoMonHocResponse>> getDanhSach() {
        return danhSach;
    }

    // ====================================================
    // 2) LẤY BÀI ĐÃ LÀM (2 cách gọi - tên method khác nhau)
    // ====================================================
    public void loadDanhSachDaLam(String maMonHoc, String maNguoiDung) {
        danhSachDaLam = repository.getDanhSachCungCoDaLam(maMonHoc, maNguoiDung);
    }

    // Method khác - gọi với maNguoiDung đã set trước đó
    public void loadCungCoDaLam(String maMonHoc) {
        if (maNguoiDung != null) {
            loadDanhSachDaLam(maMonHoc, maNguoiDung);
        }
    }

    public LiveData<List<CungCoDaLamResponse>> getDanhSachDaLam() {
        return danhSachDaLam;
    }

    // ====================================================
    // 3) SETTER MaNguoiDung
    // ====================================================
    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }
}