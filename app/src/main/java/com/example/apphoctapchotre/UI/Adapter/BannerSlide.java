package com.example.apphoctapchotre.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.CungCo.CungCoActivity;
import com.example.apphoctapchotre.UI.Activity.OnLuyen.OnLuyen;

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

        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle);
        holder.image.setImageResource(item.imageRes);

        View.OnClickListener listener = v -> {
            Context context = v.getContext();
            Intent intent = null;

            if (position == 0) {
                // Banner 1: Củng cố kiến thức
                intent = new Intent(context, CungCoActivity.class);
            } else if (position == 1) {
                // Banner 2: Ôn luyện
                intent = new Intent(context, OnLuyen.class);
            }

            if (intent != null) context.startActivity(intent);
        };

        // 👉 Gán sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(listener);
        holder.image.setOnClickListener(listener);
        holder.title.setOnClickListener(listener);
        holder.subtitle.setOnClickListener(listener);
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
