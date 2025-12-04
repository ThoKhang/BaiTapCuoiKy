package com.example.apphoctapchotre.UI.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.LichSuRepository;
import com.example.apphoctapchotre.DATA.model.DiemChiTiet;
import com.example.apphoctapchotre.DATA.model.LichSuDiemResponse;

import java.util.ArrayList;
import java.util.List;

public class LichSuViewModel extends ViewModel {

    private final LichSuRepository repository;

    private final MutableLiveData<Integer> tongDiemLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> diemKiemTraLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> diemHoatDongLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<List<DiemChiTiet>> danhSachChiTietLiveData =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> dangTaiLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> loiLiveData = new MutableLiveData<>();

    public LichSuViewModel() {
        repository = new LichSuRepository();
    }

    public LiveData<Integer> getTongDiem() {
        return tongDiemLiveData;
    }

    public LiveData<Integer> getDiemKiemTra() {
        return diemKiemTraLiveData;
    }

    public LiveData<Integer> getDiemHoatDong() {
        return diemHoatDongLiveData;
    }

    public LiveData<List<DiemChiTiet>> getDanhSachChiTiet() {
        return danhSachChiTietLiveData;
    }

    public LiveData<Boolean> getDangTaiLiveData() {
        return dangTaiLiveData;
    }

    public LiveData<String> getLoiLiveData() {
        return loiLiveData;
    }

    public void taiLichSuDiem(String email) {
        if (email == null || email.isEmpty()) {
            loiLiveData.setValue("Không tìm thấy người dùng, vui lòng đăng nhập lại!");
            return;
        }

        dangTaiLiveData.setValue(true);
        loiLiveData.setValue(null);

        repository.layLichSuDiem(email, new LichSuRepository.RepositoryCallback<LichSuDiemResponse>() {
            @Override
            public void onSuccess(LichSuDiemResponse duLieu) {
                dangTaiLiveData.setValue(false);

                tongDiemLiveData.setValue(duLieu.getTongDiem());
                diemKiemTraLiveData.setValue(duLieu.getDiemKiemTra());
                diemHoatDongLiveData.setValue(duLieu.getDiemHoatDong());

                List<DiemChiTiet> list = duLieu.getDanhSachChiTiet();
                if (list == null) list = new ArrayList<>();
                danhSachChiTietLiveData.setValue(list);
            }

            @Override
            public void onError(String message) {
                dangTaiLiveData.setValue(false);
                loiLiveData.setValue(message);
            }
        });
    }
}
