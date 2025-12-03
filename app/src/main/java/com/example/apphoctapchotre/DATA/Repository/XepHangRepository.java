package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.XepHangResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XepHangRepository {

    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    private final ApiService apiService;

    public XepHangRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void layXepHang(String email, int gioiHan,
                           RepositoryCallback<XepHangResponse> callback) {

        apiService.layXepHang(email, gioiHan).enqueue(new Callback<XepHangResponse>() {
            @Override
            public void onResponse(Call<XepHangResponse> call,
                                   Response<XepHangResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Không tải được bảng xếp hạng.");
                }
            }

            @Override
            public void onFailure(Call<XepHangResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
