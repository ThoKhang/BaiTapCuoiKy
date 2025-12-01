package com.example.apphoctapchotre.Adapter.LyThuyetCoBan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.LyThuyetItemResponse;
import java.util.List;

public class LyThuyetAdapter extends RecyclerView.Adapter<LyThuyetAdapter.LyThuyetViewHolder> {

    private final List<LyThuyetItemResponse> list;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public LyThuyetAdapter(Context context, List<LyThuyetItemResponse> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LyThuyetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_lythuyet, parent, false);
        return new LyThuyetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LyThuyetViewHolder holder, int position) {
        LyThuyetItemResponse item = list.get(position);

        holder.tvTieuDe.setText(item.getTieuDe());

        // CHƯA HỌC → +10 | ĐÃ HỌC → TICK VÀNG
        if (item.isDaHoanThanh()) {
            holder.tvDiem.setVisibility(View.GONE);
            holder.imgDone.setVisibility(View.VISIBLE);
        } else {
            holder.tvDiem.setVisibility(View.VISIBLE);
            holder.tvDiem.setText("+10 điểm");
            holder.imgDone.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class LyThuyetViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvDiem;
        ImageView imgDone;

        public LyThuyetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvDiem = itemView.findViewById(R.id.tvDiem);
            imgDone = itemView.findViewById(R.id.imgDone);
        }
    }
}