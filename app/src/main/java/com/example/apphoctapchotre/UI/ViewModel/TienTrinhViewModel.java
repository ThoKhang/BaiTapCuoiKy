package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.TienTrinhRepository;
import com.example.apphoctapchotre.DATA.model.TienTrinh;

import java.util.List;

public class TienTrinhViewModel extends ViewModel {
    private final TienTrinhRepository repo = TienTrinhRepository.getInstance();

    public MutableLiveData<List<TienTrinh>> listTienTrinh = new MutableLiveData<>();

    public void loadData(String email, String tieuDe) {
        repo.getSoCauDaLam(email, tieuDe).observeForever(data -> {
            listTienTrinh.setValue(data);
        });
    }
}

