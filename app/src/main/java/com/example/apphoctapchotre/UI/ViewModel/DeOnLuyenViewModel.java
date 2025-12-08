package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.DeOnLuyenRepository;
import com.example.apphoctapchotre.DATA.model.DeOnLuyen;

public class DeOnLuyenViewModel extends ViewModel {
    private DeOnLuyenRepository deOnLuyenRepository = DeOnLuyenRepository.getInstance();
    public MutableLiveData<DeOnLuyen> deOnLuyenMutableLiveData = new MutableLiveData<>();
    public void loadDeOnLuyen(String tieuDe){
        deOnLuyenRepository.getDeOnLuyen(tieuDe).observeForever(data->{
            if(data!=null)
                deOnLuyenMutableLiveData.setValue(data);
            else
                deOnLuyenMutableLiveData.setValue(null);
        });
    }
}
