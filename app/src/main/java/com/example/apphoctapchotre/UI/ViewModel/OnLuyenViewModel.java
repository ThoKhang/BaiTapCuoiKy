package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.OnLuyenRepository;
import com.example.apphoctapchotre.DATA.model.OnLuyen;

public class OnLuyenViewModel extends ViewModel {
    private final OnLuyenRepository repository=OnLuyenRepository.getInstance();
    public MutableLiveData<OnLuyen> onLuyen=new MutableLiveData<>();
    public void loadOnLuyen(String email){
        repository.getOnLuyen(email).observeForever(data->{
            if(data!=null)
                onLuyen.setValue(data);
            else
                onLuyen.setValue(null);
        });
    }
}
