package com.example.apphoctapchotre.UI.Adapter.XepHang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.XepHangItem;

import java.util.List;

public class XepHangAdapter extends RecyclerView.Adapter<XepHangAdapter.ViewHolder> {

    private List<XepHangItem> danhSachXepHang;

    public XepHangAdapter(List<XepHangItem> danhSachXepHang) {
        this.danhSachXepHang = danhSachXepHang;
    }

    // 👉 THÊM HÀM NÀY
    public void setData(List<XepHangItem> newData) {
        this.danhSachXepHang = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemRoot;
        TextView tvHang, tvTenNguoiChoi, tvTongDiem;
        ImageView imgHuyChuong, imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.itemRoot);
            tvHang = itemView.findViewById(R.id.tvRank);
            imgHuyChuong = itemView.findViewById(R.id.imgMedal);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvTenNguoiChoi = itemView.findViewById(R.id.tvUsername);
            tvTongDiem = itemView.findViewById(R.id.tvScore);
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
        XepHangItem item = danhSachXepHang.get(position);

        int hang = position + 1;

        holder.tvTenNguoiChoi.setText(item.getTenNguoiChoi());
        holder.tvTongDiem.setText(String.format("%,d", item.getTongDiem()));

        // ==========================
        //   HIỂN THỊ THEO THỨ HẠNG
        // ==========================
        switch (hang) {
            case 1:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_1);
                holder.imgHuyChuong.setVisibility(View.VISIBLE);
                holder.imgHuyChuong.setImageResource(R.drawable.top1);
                holder.tvHang.setVisibility(View.GONE);
                break;

            case 2:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_2);
                holder.imgHuyChuong.setVisibility(View.VISIBLE);
                holder.imgHuyChuong.setImageResource(R.drawable.top2);
                holder.tvHang.setVisibility(View.GONE);
                break;

            case 3:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_3);
                holder.imgHuyChuong.setVisibility(View.VISIBLE);
                holder.imgHuyChuong.setImageResource(R.drawable.top3);
                holder.tvHang.setVisibility(View.GONE);
                break;

            default:
                holder.itemRoot.setBackgroundResource(R.drawable.bg_rank_normal);
                holder.imgHuyChuong.setVisibility(View.GONE);
                holder.tvHang.setVisibility(View.VISIBLE);
                holder.tvHang.setText(String.valueOf(hang));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return danhSachXepHang != null ? danhSachXepHang.size() : 0;
    }
}
