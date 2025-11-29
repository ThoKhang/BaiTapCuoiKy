package com.example.apphoctapchotre.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.KhoaHocGioiThieu;

import java.util.List;

public class KhoaHocGioiThieuAdapter extends RecyclerView.Adapter<KhoaHocGioiThieuAdapter.KhoaHocViewHolder> {

    private List<KhoaHocGioiThieu> danhSachKhoaHoc;

    public KhoaHocGioiThieuAdapter(List<KhoaHocGioiThieu> danhSachKhoaHoc) {
        this.danhSachKhoaHoc = danhSachKhoaHoc;
    }

    @NonNull
    @Override
    public KhoaHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_khoahoc, parent, false);
        return new KhoaHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoaHocViewHolder holder, int position) {
        KhoaHocGioiThieu khoaHoc = danhSachKhoaHoc.get(position);

        holder.txtTenKhoaHoc.setText(khoaHoc.getTenKhoaHoc());
        holder.ratingBar.setRating(khoaHoc.getDiemDanhGia());
        holder.txtDiem.setText(String.valueOf(khoaHoc.getDiemDanhGia()));
        holder.imgHinhAnh.setImageResource(khoaHoc.getHinhAnh());
    }

    @Override
    public int getItemCount() {
        return danhSachKhoaHoc.size();
    }

    public static class KhoaHocViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHinhAnh;
        TextView txtTenKhoaHoc, txtDiem;
        RatingBar ratingBar;

        public KhoaHocViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhAnh = itemView.findViewById(R.id.imgKhoaHoc);
            txtTenKhoaHoc = itemView.findViewById(R.id.txtKhoaHoc);
            txtDiem = itemView.findViewById(R.id.txtRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}