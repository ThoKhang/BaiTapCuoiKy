package com.example.apphoctapchotre.Adapter.DiemChiTiet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.DiemChiTiet;

import java.util.List;

// Adapter cho danh sách chi tiết điểm
public class AdapterDiemChiTiet extends RecyclerView.Adapter<AdapterDiemChiTiet.ViewHolder> {
    private List<DiemChiTiet> danhSachDiem;  // Danh sách dữ liệu

    // Constructor
    public AdapterDiemChiTiet(List<DiemChiTiet> danhSachDiem) {
        this.danhSachDiem = danhSachDiem;
    }

    // Lớp giữ view của item (ViewHolder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSoDiem;
        TextView txtThoiGian;
        TextView txtThongTin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSoDiem = itemView.findViewById(R.id.txtSoDiem);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtThongTin = itemView.findViewById(R.id.txtThongTin);
        }
    }

    // Tạo view holder (inflate layout item)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diem_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    // Bind dữ liệu vào view (tại vị trí position)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiemChiTiet diem = danhSachDiem.get(position);
        holder.txtSoDiem.setText(diem.getSoDiem());
        holder.txtThoiGian.setText(diem.getThoiGian());
        holder.txtThongTin.setText(diem.getThongTin());
    }

    // Số lượng item trong danh sách
    @Override
    public int getItemCount() {
        return danhSachDiem.size();
    }
}