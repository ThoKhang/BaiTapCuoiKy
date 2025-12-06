package com.example.apphoctapchotre.UI.ViewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OnLuyenViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public OnLuyenViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OnLuyenViewModel.class)) {
            return (T) new OnLuyenViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
