package com.example.apphoctapchotre.Adapter.OnLuyen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.DeItem;

import java.util.List;

public class DeAdapter extends RecyclerView.Adapter<DeAdapter.DeViewHolder> {

    private List<DeItem> list;
    private Context context;
    private int bgResource;     // gradient theo loại đề

    public DeAdapter(Context context, List<DeItem> list, int bgResource) {
        this.context = context;
        this.list = list;
        this.bgResource = bgResource;
    }

    @NonNull
    @Override
    public DeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_de, parent, false);
        return new DeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeViewHolder h, int position) {
        DeItem item = list.get(position);

        h.tvTieuDe.setText(item.getTieuDe());
        h.itemContainer.setBackgroundResource(bgResource);

        if (item.getSoCauDung() > 0) {
            h.tvDiem.setBackgroundResource(R.drawable.circle);
            h.tvDiem.setText(item.getSoCauDung() + "/" + item.getTongCau());
            h.tvDiem.setTextColor(context.getColor(R.color.white));
        } else {
            h.tvDiem.setBackground(null);
            h.tvDiem.setText("+" + item.getDiemThuong() + " điểm");
            h.tvDiem.setTextColor(context.getColor(R.color.yellow_custom));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvDiem;
        FrameLayout itemContainer;

        public DeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvDiem = itemView.findViewById(R.id.tvDiem);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }
    }
}
