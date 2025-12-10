package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.LyThuyetRepository;
import com.example.apphoctapchotre.DATA.model.LyThuyetDaLamResponse;
import com.example.apphoctapchotre.DATA.model.LyThuyetMonHocResponse;

import java.util.List;

public class LyThuyetViewModel2 extends ViewModel {

    private final LyThuyetRepository repository = new LyThuyetRepository();

    // LiveData ổn định — KHÔNG bị thay đổi reference
    private final MutableLiveData<List<LyThuyetMonHocResponse>> danhSach = new MutableLiveData<>();
    private final MutableLiveData<List<LyThuyetDaLamResponse>> danhSachDaLam = new MutableLiveData<>();

    private String maNguoiDung;

    // ======================================================
    // 1) LOAD DANH SÁCH BÀI CHƯA HỌC
    // ======================================================
    public void loadDanhSach(String maMonHoc) {
        repository.getDanhSachLyThuyet(maMonHoc).observeForever(data -> {
            danhSach.setValue(data);
        });
    }

    public LiveData<List<LyThuyetMonHocResponse>> getDanhSach() {
        return danhSach;
    }

    // ======================================================
    // 2) LOAD DANH SÁCH BÀI ĐÃ HỌC
    // ======================================================
    public void loadDanhSachDaLam(String maMonHoc, String maNguoiDung) {
        repository.getDanhSachLyThuyetDaLam(maMonHoc, maNguoiDung)
                .observeForever(data -> {
                    danhSachDaLam.setValue(data);
                });
    }

    public void loadLyThuyetDaLam(String maMonHoc) {
        if (maNguoiDung != null) {
            loadDanhSachDaLam(maMonHoc, maNguoiDung);
        }
    }

    public LiveData<List<LyThuyetDaLamResponse>> getDanhSachDaLam() {
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