package com.example.apphoctapchotre.UI.Adapter.LyThuyetCoBan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.ui.LyThuyetItem;

import java.util.List;

// Trong file LyThuyetAdapter.java

public class LyThuyetAdapter extends RecyclerView.Adapter<LyThuyetAdapter.LyThuyetViewHolder> {

    private final List<LyThuyetItem> list;
    private final Context context;
    private final OnItemClickListener listener;  // Thêm dòng này

    // Interface để truyền sự kiện click ra ngoài
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor mới có thêm listener
    public LyThuyetAdapter(Context context, List<LyThuyetItem> list, OnItemClickListener listener) {
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
        LyThuyetItem item = list.get(position);

        holder.tvTieuDe.setText(item.getTieuDe());
        holder.tvDiem.setText("+" + item.getDiemThuong() + " điểm");

        // Xử lý click ở đây
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class LyThuyetViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvDiem;
        FrameLayout itemContainer;

        public LyThuyetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvDiem = itemView.findViewById(R.id.tvDiem);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }
    }
}