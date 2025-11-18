package com.example.apphoctapchotre.Adapter.XepHang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.XepHangItem;

import java.util.List;

public class XepHangAdapter extends RecyclerView.Adapter<XepHangAdapter.ViewHolder> {

    private List<XepHangItem> list;

    public XepHangAdapter(List<XepHangItem> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemRoot;
        TextView tvRank, tvUsername, tvScore;
        ImageView imgMedal;
        ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.itemRoot);
            tvRank = itemView.findViewById(R.id.tvRank);
            imgMedal = itemView.findViewById(R.id.imgMedal);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_xep_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        XepHangItem item = list.get(position);
        int rank = position + 1;

        holder.tvUsername.setText(item.getUsername());
        holder.tvScore.setText(String.format("%,d", item.getScore()));

        switch (rank) {
            case 1:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_1);
                holder.imgMedal.setImageResource(R.drawable.top1); // bạn tự tạo 3 icon huy chương hoặc dùng emoji cũng được
                holder.imgMedal.setVisibility(View.VISIBLE);
                holder.tvRank.setVisibility(View.GONE);
                break;
            case 2:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_2);
                holder.imgMedal.setImageResource(R.drawable.top2);
                holder.imgMedal.setVisibility(View.VISIBLE);
                holder.tvRank.setVisibility(View.GONE);
                break;
            case 3:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_3);
                holder.imgMedal.setImageResource(R.drawable.top3);
                holder.imgMedal.setVisibility(View.VISIBLE);
                holder.tvRank.setVisibility(View.GONE);
                break;
            default:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_normal);
                holder.imgMedal.setVisibility(View.GONE);
                holder.tvRank.setVisibility(View.VISIBLE);
                holder.tvRank.setText(String.valueOf(rank));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}