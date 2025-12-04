package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.CungCoRepository;
import com.example.apphoctapchotre.DATA.model.CungCoResponse;

import java.util.List;

public class CungCoViewModel extends ViewModel {

    private CungCoRepository repository;
    private LiveData<List<CungCoResponse>> tienDoLiveData;

    public CungCoViewModel() {
        repository = new CungCoRepository();
    }

    public void loadTienDo(String maNguoiDung) {
        tienDoLiveData = repository.getTienDo(maNguoiDung);
    }

    public LiveData<List<CungCoResponse>> getTienDo() {
        return tienDoLiveData;
    }
}

