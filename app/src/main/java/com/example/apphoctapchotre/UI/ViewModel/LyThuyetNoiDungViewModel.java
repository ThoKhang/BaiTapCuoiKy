package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.LyThuyetNoiDungRepository;
import com.example.apphoctapchotre.DATA.model.LyThuyetNoiDungResponse;

public class LyThuyetNoiDungViewModel extends ViewModel {

    private final LyThuyetNoiDungRepository repository = new LyThuyetNoiDungRepository();
    private LiveData<LyThuyetNoiDungResponse> noiDung;

    public void loadNoiDung(String maHoatDong) {
        noiDung = repository.getNoiDung(maHoatDong);
    }

    public LiveData<LyThuyetNoiDungResponse> getNoiDung() {
        return noiDung;
    }
}