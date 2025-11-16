package com.example.apphoctapchotre;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Activity.CungCo.cungcokienthuc;
import com.example.apphoctapchotre.Activity.OnLuyen.OnLuyen;

import java.util.List;

public class BannerSlide extends RecyclerView.Adapter<BannerSlide.BannerViewHolder> {

    private final List<BannerItem> bannerList;

    public BannerSlide(List<BannerItem> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        BannerItem item = bannerList.get(position);

        // Gán dữ liệu cho banner
        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle);

        holder.subtitle.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent;

            switch (position) {
                case 1:
                    intent = new Intent(context, OnLuyen.class);
                    break;
                case 2:
                    intent = new Intent(context, cungcokienthuc.class);
                    break;
                default:
                    intent = null;
            }

            if (intent != null) context.startActivity(intent);
        });


        // Gán ảnh minh họa khác nhau cho từng banner
        holder.image.setImageResource(item.imageRes);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    // ---------------------- Model ----------------------
    public static class BannerItem {
        int imageRes;
        String title, subtitle;

        public BannerItem(int imageRes, String title, String subtitle) {
            this.imageRes = imageRes;
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    // ---------------------- ViewHolder ----------------------
    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, subtitle;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bannerImage);
            title = itemView.findViewById(R.id.bannerTitle);
            subtitle = itemView.findViewById(R.id.bannerSubtitle);
        }
    }
}
