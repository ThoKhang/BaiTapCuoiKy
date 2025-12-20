package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.DeOnLuyenRepository;
import com.example.apphoctapchotre.DATA.model.DeOnLuyen;
import com.example.apphoctapchotre.DATA.model.TienTrinh;

public class LienHoanTinhToanViewModel extends ViewModel {

    private DeOnLuyenRepository repo = DeOnLuyenRepository.getInstance();

    public MutableLiveData<DeOnLuyen> deLienHoan = new MutableLiveData<>();
    public MutableLiveData<Boolean> ketQuaTaoTienTrinh = new MutableLiveData<>();

    public void loadDeLienHoanTinhToan() {
        repo.getDeLienHoanTinhToan().observeForever(data -> {
            if (data != null)
                deLienHoan.setValue(data);
            else
                deLienHoan.setValue(null);
        });
    }

    public void guiTienTrinh(TienTrinh tienTrinh) {
        repo.taoTienTrinh(tienTrinh).observeForever(kq -> {
            ketQuaTaoTienTrinh.setValue(kq);
        });
    }
}