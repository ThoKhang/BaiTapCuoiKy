package com.example.apphoctapchotre;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apphoctapchotre.model.BaiKiemTra;

import java.util.List;

public class BaiKiemTraAdapter extends BaseAdapter {

    private final Context context;
    private final List<BaiKiemTra> danhSach;
    private final int maMonHoc;

    public BaiKiemTraAdapter(Context context, List<BaiKiemTra> danhSach, int maMonHoc) {
        this.context = context;
        this.danhSach = danhSach;
        this.maMonHoc = maMonHoc;
    }

    @Override
    public int getCount() {
        return danhSach.size();
    }

    @Override
    public Object getItem(int position) {
        return danhSach.get(position);
    }

    @Override
    public long getItemId(int position) {
        return danhSach.get(position).getMaBaiKiemTra();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.activity_item_mon_hoc, parent, false);
        }

        BaiKiemTra bai = danhSach.get(position);
        TextView tvTieuDePhu = convertView.findViewById(R.id.tvTieuDePhu);
        TextView tvDiem = convertView.findViewById(R.id.tvDiem);
        View itemLayout = convertView.findViewById(R.id.itemLayout);

        // 🔹 Hiển thị tiêu đề phụ
        if (bai.getTieuDePhu() != null && !bai.getTieuDePhu().isEmpty()) {
            tvTieuDePhu.setText(bai.getTieuDePhu());
        } else {
            tvTieuDePhu.setText("Bài củng cố khác");
        }

        // 🔹 Hiển thị trạng thái điểm / hoàn thành
        if (bai.isDaHoanThanh()) {
            tvDiem.setText("✅ Hoàn thành");
            tvDiem.setTextColor(Color.parseColor("#00C853")); // Xanh lá
        } else {
            tvDiem.setText("+50 điểm");
            tvDiem.setTextColor(Color.parseColor("#FFA000")); // Vàng cam
        }

        // 🔹 Đổi màu nền item theo môn học
        if (maMonHoc == 1) {
            itemLayout.setBackgroundResource(R.drawable.bg_banner_green); // Toán
        } else if (maMonHoc == 2) {
            itemLayout.setBackgroundResource(R.drawable.bg_banner_blue); // Tiếng Việt
        } else {
            itemLayout.setBackgroundResource(R.drawable.bg_banner_blue);
        }

        return convertView;
    }

    // 🔹 Hàm đánh dấu hoàn thành một bài
    public void danhDauHoanThanh(int maTieuDePhu) {
        for (BaiKiemTra bai : danhSach) {
            if (bai.getMaTieuDePhu() == maTieuDePhu) {
                bai.setDaHoanThanh(true);
                break;
            }
        }
        notifyDataSetChanged();
    }
}
