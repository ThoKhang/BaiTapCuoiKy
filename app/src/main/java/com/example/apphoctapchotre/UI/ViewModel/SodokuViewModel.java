package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.DeOnLuyenRepository;
import com.example.apphoctapchotre.DATA.model.TienTrinh;

public class SodokuViewModel extends ViewModel {
    private DeOnLuyenRepository repo = DeOnLuyenRepository.getInstance();
    public MutableLiveData<Boolean> ketQuaTaoTienTrinh = new MutableLiveData<>();

    public void guiTienTrinh(TienTrinh tienTrinh) {
        repo.taoTienTrinh(tienTrinh).observeForever(kq -> {
            ketQuaTaoTienTrinh.setValue(kq);
        });
    }
}
