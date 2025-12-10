package com.example.apphoctapchotre.DATA.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TienTrinhRepository {
    private static TienTrinhRepository instance;
    private ApiService apiService= RetrofitClient.getClient().create(ApiService.class);
    public static TienTrinhRepository getInstance(){
        if(instance==null)
            instance=new TienTrinhRepository();
        return instance;
    }
    public LiveData<List<TienTrinh>> getSoCauDaLam(String email, String tieuDe){
        MutableLiveData<List<TienTrinh>> liveData = new MutableLiveData<>();
        apiService.soCauDaLam(email, tieuDe).enqueue(new Callback<List<TienTrinh>>() {
            @Override
            public void onResponse(Call<List<TienTrinh>> call, Response<List<TienTrinh>> response) {
                Log.d("TienTrinhRepo", "RAW response: " + new Gson().toJson(response.body()));
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<TienTrinh>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }
}
