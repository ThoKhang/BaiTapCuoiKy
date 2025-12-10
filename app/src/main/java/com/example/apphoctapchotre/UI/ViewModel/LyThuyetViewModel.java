package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.LyThuyetRepository;
import com.example.apphoctapchotre.DATA.model.LyThuyetResponse;

import java.util.ArrayList;
import java.util.List;

public class LyThuyetViewModel extends ViewModel {

    private final LyThuyetRepository repository = new LyThuyetRepository();

    // KHỞI TẠO LUÔN ĐỂ TRÁNH NULL
    private final MutableLiveData<List<LyThuyetResponse>> tienDoLiveData =
            new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<LyThuyetResponse>> getTienDo() {
        return tienDoLiveData;
    }

    // LUÔN GÁN GIÁ TRỊ CHO LiveData
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