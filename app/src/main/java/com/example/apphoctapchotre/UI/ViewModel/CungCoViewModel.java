package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.CungCoRepository;
import com.example.apphoctapchotre.DATA.model.CungCoResponse;

import java.util.ArrayList;
import java.util.List;

public class CungCoViewModel extends ViewModel {

    private final CungCoRepository repository = new CungCoRepository();

    // 🔥 KHỞI TẠO LUÔN ĐỂ TRÁNH NULL
    private final MutableLiveData<List<CungCoResponse>> tienDoLiveData =
            new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<CungCoResponse>> getTienDo() {
        return tienDoLiveData;
    }

    // 🔥 LUÔN GÁN GIÁ TRỊ CHO LiveData
    public void loadTienDo(String maNguoiDung) {
        repository.getTienDo(maNguoiDung).observeForever(list -> {
            if (list != null) {
                tienDoLiveData.setValue(list);
            } else {
                tienDoLiveData.setValue(new ArrayList<>()); // tránh null gây crash
            }
        });
    }
}
