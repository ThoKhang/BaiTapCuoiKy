package com.example.apphoctapchotre.DATA.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.Media;
import com.example.apphoctapchotre.DATA.model.MediaProgressRequest;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient; // ✅ DÒNG QUAN TRỌNG

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaRepository {

    private ApiService api;

    public MediaRepository() {
        api = RetrofitClient.getClient().create(ApiService.class);
    }

    public LiveData<List<Media>> getAudioList() {
        MutableLiveData<List<Media>> data = new MutableLiveData<>();

        api.getMediaByLoai("AUDIO").enqueue(new Callback<List<Media>>() {
            @Override
            public void onResponse(Call<List<Media>> call, Response<List<Media>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Media>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public void saveProgress(Long maMedia, String email, int viTriGiay, boolean daXem) {
        api.saveProgress(
                maMedia,
                email,
                new MediaProgressRequest(viTriGiay, daXem)
        ).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {}
            @Override public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}
