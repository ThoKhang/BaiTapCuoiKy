import { useEffect, useState } from "react";
import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
  resetPassword
} from "../api/userApi";

export default function Users() {
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState({
    maNguoiDung: "",
    tenDangNhap: "",
    email: ""
  });
  const [editingId, setEditingId] = useState(null);
  const [newPassword, setNewPassword] = useState("");

  const loadData = async () => {
    const data = await getUsers();
    setUsers(data);
  };

  useEffect(() => {
  const fetchData = async () => {
    const data = await getUsers();
    setUsers(data);
  };

  fetchData();
}, []);


  const handleSubmit = async () => {
    if (editingId) {
      await updateUser(editingId, form);
    } else {
      await createUser(form);
    }
    setForm({ maNguoiDung: "", tenDangNhap: "", email: "" });
    setEditingId(null);
    loadData();
  };

  const handleEdit = (u) => {
    setEditingId(u.maNguoiDung);
    setForm({
      maNguoiDung: u.maNguoiDung,
      tenDangNhap: u.tenDangNhap,
      email: u.email
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xóa người dùng?")) {
      await deleteUser(id);
      loadData();
    }
  };

  const handleResetPassword = async (id) => {
    if (!newPassword) {
      alert("Nhập mật khẩu mới");
      return;
    }
    await resetPassword(id, newPassword);
    alert("Đã reset mật khẩu");
    setNewPassword("");
  };

  return (
    <div>
      <h2>👨‍🎓 Quản lý người dùng</h2>

      {/* FORM */}
      <div style={{ marginBottom: 20 }}>
        <input
          placeholder="Mã"
          value={form.maNguoiDung}
          onChange={e => setForm({ ...form, maNguoiDung: e.target.value })}
          disabled={!!editingId}
        />
        <input
          placeholder="Tên đăng nhập"
          value={form.tenDangNhap}
          onChange={e => setForm({ ...form, tenDangNhap: e.target.value })}
        />
        <input
          placeholder="Email"
          value={form.email}
          onChange={e => setForm({ ...form, email: e.target.value })}
        />
        <button onClick={handleSubmit}>
          {editingId ? "Cập nhật" : "Thêm"}
        </button>
      </div>

      {/* TABLE */}
      <table border="1" cellPadding="5">
        <thead>
          <tr>
            <th>Mã</th>
            <th>Tên đăng nhập</th>
            <th>Email</th>
            <th>Điểm</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {users.map(u => (
            <tr key={u.maNguoiDung}>
              <td>{u.maNguoiDung}</td>
              <td>{u.tenDangNhap}</td>
              <td>{u.email}</td>
              <td>{u.tongDiem}</td>
              <td>
                <button onClick={() => handleEdit(u)}>Sửa</button>
                <button onClick={() => handleDelete(u.maNguoiDung)}>Xóa</button>
                <br />
                <input
                  placeholder="Mật khẩu mới"
                  value={newPassword}
                  onChange={e => setNewPassword(e.target.value)}
                />
                <button onClick={() => handleResetPassword(u.maNguoiDung)}>
                  Reset PW
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
