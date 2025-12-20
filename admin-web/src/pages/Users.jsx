import { useEffect, useState } from "react";
import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
  resetPassword
} from "../api/userApi";
import "./Users.css";

export default function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [editingId, setEditingId] = useState(null);
  const [resetPasswordId, setResetPasswordId] = useState(null);
  const [newPassword, setNewPassword] = useState("");
  const [form, setForm] = useState({
    maNguoiDung: "",
    tenDangNhap: "",
    email: ""
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const data = await getUsers();
      setUsers(data);
    } catch (error) {
      console.error("Error:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async () => {
    if (!form.maNguoiDung.trim() || !form.tenDangNhap.trim() || !form.email.trim()) {
      alert("Vui lòng nhập đầy đủ thông tin");
      return;
    }

    setLoading(true);
    try {
      if (editingId) {
        await updateUser(editingId, form);
      } else {
        await createUser(form);
      }
      resetForm();
      setShowModal(false);
      loadData();
    } catch (error) {
      alert("Lỗi: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (u) => {
    setEditingId(u.maNguoiDung);
    setForm({
      maNguoiDung: u.maNguoiDung,
      tenDangNhap: u.tenDangNhap,
      email: u.email
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn chắc chắn muốn xóa người dùng này?")) {
      setLoading(true);
      try {
        await deleteUser(id);
        loadData();
      } catch (error) {
        alert("Lỗi xóa: " + error.message);
      } finally {
        setLoading(false);
      }
    }
  };

  const handleResetPassword = async (id) => {
    if (!newPassword.trim()) {
      alert("Vui lòng nhập mật khẩu mới");
      return;
    }

    setLoading(true);
    try {
      await resetPassword(id, newPassword);
      alert("✅ Đã reset mật khẩu thành công");
      setNewPassword("");
      setResetPasswordId(null);
    } catch (error) {
      alert("Lỗi: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setForm({
      maNguoiDung: "",
      tenDangNhap: "",
      email: ""
    });
    setEditingId(null);
  };

  const handleCloseModal = () => {
    resetForm();
    setShowModal(false);
  };

  const filteredUsers = users.filter(u =>
    (u.maNguoiDung && u.maNguoiDung.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (u.tenDangNhap && u.tenDangNhap.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (u.email && u.email.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  return (
    <div className="users-container">
      {/* Header */}
      <div className="page-header">
        <div className="header-left">
          <h1>👥 Quản lý người dùng</h1>
          <p className="subtitle">Tổng cộng: <span className="badge-count">{users.length}</span> người dùng</p>
        </div>
        <button 
          className="btn-primary btn-lg"
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
        >
          + Thêm người dùng
        </button>
      </div>

      {/* Search Bar */}
      <div className="search-section">
        <div className="search-box">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Tìm kiếm theo mã, tên đăng nhập hoặc email..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>
        <p className="result-count">Kết quả: {filteredUsers.length} người dùng</p>
      </div>

      {/* Table Section */}
      <div className="table-container">
        {loading && <div className="loading-spinner">⏳ Đang tải...</div>}
        
        {!loading && filteredUsers.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">👤</div>
            <p>{searchTerm ? "Không tìm thấy người dùng" : "Chưa có người dùng"}</p>
            <button className="btn-secondary" onClick={() => setShowModal(true)}>
              Thêm người dùng đầu tiên
            </button>
          </div>
        ) : (
          !loading && (
            <table className="data-table">
              <thead>
                <tr>
                  <th className="col-index">STT</th>
                  <th className="col-code">Mã</th>
                  <th className="col-username">Tên đăng nhập</th>
                  <th className="col-email">Email</th>
                  <th className="col-score">Điểm</th>
                  <th className="col-action">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredUsers.map((u, index) => (
                  <tr key={u.maNguoiDung} className="table-row">
                    <td className="col-index">{index + 1}</td>
                    <td className="col-code">
                      <span className="badge-code">{u.maNguoiDung}</span>
                    </td>
                    <td className="col-username">
                      <div className="user-name">{u.tenDangNhap}</div>
                    </td>
                    <td className="col-email">
                      <span className="email-text">{u.email}</span>
                    </td>
                    <td className="col-score">
                      <span className="score-badge">⭐ {u.tongDiem || 0}</span>
                    </td>
                    <td className="col-action">
                      <div className="action-buttons">
                        <button 
                          className="btn-icon btn-edit"
                          onClick={() => handleEdit(u)}
                          title="Chỉnh sửa"
                        >
                          ✏️
                        </button>
                        <button 
                          className="btn-icon btn-key"
                          onClick={() => setResetPasswordId(u.maNguoiDung)}
                          title="Reset mật khẩu"
                        >
                          🔑
                        </button>
                        <button 
                          className="btn-icon btn-delete"
                          onClick={() => handleDelete(u.maNguoiDung)}
                          title="Xóa"
                        >
                          🗑️
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )
        )}
      </div>

      {/* Reset Password Modal */}
      {resetPasswordId && (
        <div className="modal-overlay" onClick={() => setResetPasswordId(null)}>
          <div className="modal-content modal-small" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>🔑 Đặt lại mật khẩu</h2>
              <button className="btn-close" onClick={() => setResetPasswordId(null)}>✕</button>
            </div>

            <div className="modal-body">
              <p className="reset-info">
                Nhập mật khẩu mới cho người dùng: <strong>{filteredUsers.find(u => u.maNguoiDung === resetPasswordId)?.tenDangNhap}</strong>
              </p>
              <div className="form-group">
                <label htmlFor="newPassword">Mật khẩu mới *</label>
                <input
                  id="newPassword"
                  type="password"
                  placeholder="Nhập mật khẩu mới..."
                  value={newPassword}
                  onChange={e => setNewPassword(e.target.value)}
                  className="form-input"
                />
              </div>
            </div>

            <div className="modal-footer">
              <button 
                className="btn-secondary"
                onClick={() => setResetPasswordId(null)}
              >
                Hủy
              </button>
              <button 
                className="btn-primary"
                onClick={() => handleResetPassword(resetPasswordId)}
                disabled={loading}
              >
                {loading ? "Đang xử lý..." : "Đặt lại mật khẩu"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Add/Edit User Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editingId ? "✏️ Chỉnh sửa người dùng" : "➕ Thêm người dùng mới"}</h2>
              <button className="btn-close" onClick={handleCloseModal}>✕</button>
            </div>

            <div className="modal-body">
              <div className="form-group">
                <label htmlFor="maNguoiDung">Mã người dùng *</label>
                <input
                  id="maNguoiDung"
                  type="text"
                  placeholder="VD: USR001"
                  value={form.maNguoiDung}
                  onChange={e => setForm({ ...form, maNguoiDung: e.target.value })}
                  className="form-input"
                  disabled={editingId !== null}
                />
              </div>

              <div className="form-group">
                <label htmlFor="tenDangNhap">Tên đăng nhập *</label>
                <input
                  id="tenDangNhap"
                  type="text"
                  placeholder="VD: nguyenvana"
                  value={form.tenDangNhap}
                  onChange={e => setForm({ ...form, tenDangNhap: e.target.value })}
                  className="form-input"
                />
              </div>

              <div className="form-group">
                <label htmlFor="email">Email *</label>
                <input
                  id="email"
                  type="email"
                  placeholder="VD: user@example.com"
                  value={form.email}
                  onChange={e => setForm({ ...form, email: e.target.value })}
                  className="form-input"
                />
              </div>
            </div>

            <div className="modal-footer">
              <button 
                className="btn-secondary"
                onClick={handleCloseModal}
              >
                Hủy
              </button>
              <button 
                className="btn-primary"
                onClick={handleSubmit}
                disabled={loading}
              >
                {loading ? "Đang xử lý..." : editingId ? "Cập nhật" : "Thêm"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}