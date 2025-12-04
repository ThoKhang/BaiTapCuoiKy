package com.example.apphoctapchotre.DATA.Repository;

import com.example.apphoctapchotre.DATA.model.LichSuDiemResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuRepository {

    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    private final ApiService apiService;

    public LichSuRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void layLichSuDiem(String email, RepositoryCallback<LichSuDiemResponse> callback) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        apiService.layLichSuDiem(body).enqueue(new Callback<LichSuDiemResponse>() {
            @Override
            public void onResponse(Call<LichSuDiemResponse> call,
                                   Response<LichSuDiemResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String msg = "Lỗi tải lịch sử điểm!";
                    try {
                        if (response.errorBody() != null) {
                            msg = response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    callback.onError(msg);
                }
            }

            @Override
            public void onFailure(Call<LichSuDiemResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
