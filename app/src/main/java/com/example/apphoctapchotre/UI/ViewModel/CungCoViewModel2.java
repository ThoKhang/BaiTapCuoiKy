package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.CungCoRepository;
import com.example.apphoctapchotre.DATA.model.CungCoDaLamResponse;
import com.example.apphoctapchotre.DATA.model.CungCoMonHocResponse;

import java.util.List;

public class CungCoViewModel2 extends ViewModel {

    private final CungCoRepository repository = new CungCoRepository();

    // LiveData ổn định — KHÔNG bị thay đổi reference
    private final MutableLiveData<List<CungCoMonHocResponse>> danhSach = new MutableLiveData<>();
    private final MutableLiveData<List<CungCoDaLamResponse>> danhSachDaLam = new MutableLiveData<>();

    private String maNguoiDung;

    // ======================================================
    // 1) LOAD DANH SÁCH BÀI CHƯA LÀM
    // ======================================================
    public void loadDanhSach(String maMonHoc) {
        repository.getDanhSachCungCo(maMonHoc).observeForever(data -> {
            danhSach.setValue(data);
        });
    }

    public LiveData<List<CungCoMonHocResponse>> getDanhSach() {
        return danhSach;
    }

    // ======================================================
    // 2) LOAD DANH SÁCH BÀI ĐÃ LÀM
    // ======================================================
    public void loadDanhSachDaLam(String maMonHoc, String maNguoiDung) {
        repository.getDanhSachCungCoDaLam(maMonHoc, maNguoiDung)
                .observeForever(data -> {
                    danhSachDaLam.setValue(data);
                });
    }

    public void loadCungCoDaLam(String maMonHoc) {
        if (maNguoiDung != null) {
            loadDanhSachDaLam(maMonHoc, maNguoiDung);
        }
    }

    public LiveData<List<CungCoDaLamResponse>> getDanhSachDaLam() {
        return danhSachDaLam;
    }

    // ======================================================
    // 3) SET MaNguoiDung
    // ======================================================
    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }
}
