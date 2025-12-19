package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.DeOnLuyenRepository;
import com.example.apphoctapchotre.DATA.model.DeOnLuyen;
import com.example.apphoctapchotre.DATA.model.TienTrinh;

public class TrumTinhNhamViewModel extends ViewModel {

    private DeOnLuyenRepository repo = DeOnLuyenRepository.getInstance();

    public MutableLiveData<DeOnLuyen> deTinhNham = new MutableLiveData<>();
    public MutableLiveData<Boolean> ketQuaTaoTienTrinh = new MutableLiveData<>();

    public void loadDeTinhNham() {
        repo.getDeTinhNham().observeForever(data -> {
            if (data != null)
                deTinhNham.setValue(data);
            else
                deTinhNham.setValue(null);
        });
    }

    public void guiTienTrinh(TienTrinh tienTrinh) {
        repo.taoTienTrinh(tienTrinh).observeForever(kq -> {
            ketQuaTaoTienTrinh.setValue(kq);
        });
    }
}