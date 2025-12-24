package com.example.apphoctapchotre.UI.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.apphoctapchotre.DATA.Repository.MediaRepository;
import com.example.apphoctapchotre.DATA.model.Media;

import java.util.List;

public class MediaViewModel extends ViewModel {

    private final MediaRepository repository;
    private LiveData<List<Media>> videoList;

    public MediaViewModel() {
        repository = new MediaRepository();
    }

    public LiveData<List<Media>> getVideoList() {
        if (videoList == null) {
            videoList = repository.getVideoList();
        }
        return videoList;
    }
}
