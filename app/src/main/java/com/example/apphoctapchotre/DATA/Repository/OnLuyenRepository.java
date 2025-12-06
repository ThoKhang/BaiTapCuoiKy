package com.example.apphoctapchotre.DATA.Repository;

import android.content.Context;

import com.example.apphoctapchotre.DATA.model.OnLuyenTongQuan;
import com.example.apphoctapchotre.DATA.remote.OnLuyenApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnLuyenRepository {
    private OnLuyenApiService api;
    public OnLuyenRepository(Context context) {
        api = RetrofitClient.getClient(context).create(OnLuyenApiService.class);
    }
    public interface OnLuyenCallBack{
        void onSuccess(OnLuyenTongQuan data);
        void onFailed(String error);
    }
    public void getTongQuan(String maNguoiDung, OnLuyenCallBack callBack){
        api.getTongQuan(maNguoiDung).enqueue(new Callback<OnLuyenTongQuan>() {
            @Override
            public void onResponse(Call<OnLuyenTongQuan> call, Response<OnLuyenTongQuan> response) {
                if(response.isSuccessful() && response.body()!=null)
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailed("Không thể tải dữ liệu");
            }

            @Override
            public void onFailure(Call<OnLuyenTongQuan> call, Throwable t) {
                callBack.onFailed(t.getMessage());
            }
        });
    }

}
