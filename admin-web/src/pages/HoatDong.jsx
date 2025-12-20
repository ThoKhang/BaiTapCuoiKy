import { useEffect, useState } from "react";
import {
  getHoatDong,
  createHoatDong,
  updateHoatDong,
  deleteHoatDong
} from "../api/hoatDongApi";

export default function HoatDong() {
  const [list, setList] = useState([]);
  const [form, setForm] = useState({
    maHoatDong: "",
    tieuDe: "",
    tongDiemToiDa: 0,
    maMonHoc: "",
    maLoai: ""
  });
  const [editingId, setEditingId] = useState(null);

  const loadData = async () => {
    const data = await getHoatDong();
    setList(data);
  };

  useEffect(() => {
    const fetchData = async () => {
      const data = await getHoatDong();
      setList(data);
    };
  
    fetchData();
  }, []);

  const handleSubmit = async () => {
    if (!form.maHoatDong || !form.tieuDe) {
      alert("Nhập thiếu dữ liệu");
      return;
    }

    if (editingId) {
      await updateHoatDong(editingId, form);
    } else {
      await createHoatDong(form);
    }

    setForm({
      maHoatDong: "",
      tieuDe: "",
      tongDiemToiDa: 0,
      maMonHoc: "",
      maLoai: ""
    });
    setEditingId(null);
    loadData();
  };

  const handleEdit = (hd) => {
    setEditingId(hd.maHoatDong);
    setForm({
      maHoatDong: hd.maHoatDong,
      tieuDe: hd.tieuDe,
      tongDiemToiDa: hd.tongDiemToiDa,
      maMonHoc: hd.maMonHoc,
      maLoai: hd.maLoai
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xóa hoạt động này?")) {
      await deleteHoatDong(id);
      loadData();
    }
  };

  return (
    <div>
      <h2>📝 Quản lý hoạt động học tập</h2>

      {/* FORM */}
      <div style={{ marginBottom: 20 }}>
        <input
          placeholder="Mã hoạt động"
          value={form.maHoatDong}
          onChange={e => setForm({ ...form, maHoatDong: e.target.value })}
          disabled={!!editingId}
        />
        <input
          placeholder="Tiêu đề"
          value={form.tieuDe}
          onChange={e => setForm({ ...form, tieuDe: e.target.value })}
        />
        <input
          placeholder="Mã môn"
          value={form.maMonHoc}
          onChange={e => setForm({ ...form, maMonHoc: e.target.value })}
        />
        <input
          placeholder="Mã loại"
          value={form.maLoai}
          onChange={e => setForm({ ...form, maLoai: e.target.value })}
        />
        <input
          type="number"
          placeholder="Tổng điểm"
          value={form.tongDiemToiDa}
          onChange={e => setForm({ ...form, tongDiemToiDa: e.target.value })}
        />
        <button onClick={handleSubmit}>
          {editingId ? "Cập nhật" : "Thêm"}
        </button>
      </div>

      {/* TABLE */}
      <table border="1" cellPadding="6">
        <thead>
          <tr>
            <th>Mã</th>
            <th>Tiêu đề</th>
            <th>Môn</th>
            <th>Loại</th>
            <th>Điểm</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {list.map(hd => (
            <tr key={hd.maHoatDong}>
              <td>{hd.maHoatDong}</td>
              <td>{hd.tieuDe}</td>
              <td>{hd.maMonHoc}</td>
              <td>{hd.maLoai}</td>
              <td>{hd.tongDiemToiDa}</td>
              <td>
                <button onClick={() => handleEdit(hd)}>Sửa</button>
                <button onClick={() => handleDelete(hd.maHoatDong)}>Xóa</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
