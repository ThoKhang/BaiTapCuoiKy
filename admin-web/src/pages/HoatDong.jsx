import { useEffect, useState } from "react";
import {
  getHoatDong,
  createHoatDong,
  updateHoatDong,
  deleteHoatDong
} from "../api/hoatDongApi";
import "./HoatDong.css";

export default function HoatDong() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({
    maHoatDong: "",
    tieuDe: "",
    tongDiemToiDa: 0,
    maMonHoc: "",
    maLoai: ""
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const data = await getHoatDong();
      setList(data);
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
    if (!form.maHoatDong.trim() || !form.tieuDe.trim()) {
      alert("Vui lòng nhập đầy đủ thông tin");
      return;
    }

    setLoading(true);
    try {
      if (editingId) {
        await updateHoatDong(editingId, form);
      } else {
        await createHoatDong(form);
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

  const handleEdit = (hd) => {
    setEditingId(hd.maHoatDong);
    setForm({
      maHoatDong: hd.maHoatDong,
      tieuDe: hd.tieuDe,
      tongDiemToiDa: hd.tongDiemToiDa,
      maMonHoc: hd.maMonHoc,
      maLoai: hd.maLoai
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn chắc chắn muốn xóa hoạt động này?")) {
      setLoading(true);
      try {
        await deleteHoatDong(id);
        loadData();
      } catch (error) {
        alert("Lỗi xóa: " + error.message);
      } finally {
        setLoading(false);
      }
    }
  };

  const resetForm = () => {
    setForm({
      maHoatDong: "",
      tieuDe: "",
      tongDiemToiDa: 0,
      maMonHoc: "",
      maLoai: ""
    });
    setEditingId(null);
  };

  const handleCloseModal = () => {
    resetForm();
    setShowModal(false);
  };

  const filteredList = list.filter(hd =>
    (hd.maHoatDong && hd.maHoatDong.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (hd.tieuDe && hd.tieuDe.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (hd.maMonHoc && hd.maMonHoc.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  return (
    <div className="hoat-dong-container">
      {/* Header */}
      <div className="page-header">
        <div className="header-left">
          <h1>📝 Quản lý hoạt động học tập</h1>
          <p className="subtitle">Tổng cộng: <span className="badge-count">{list.length}</span> hoạt động</p>
        </div>
        <button 
          className="btn-primary btn-lg"
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
        >
          + Thêm hoạt động
        </button>
      </div>

      {/* Search Bar */}
      <div className="search-section">
        <div className="search-box">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Tìm kiếm theo mã, tiêu đề hoặc môn học..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>
        <p className="result-count">Kết quả: {filteredList.length} hoạt động</p>
      </div>

      {/* Table Section */}
      <div className="table-container">
        {loading && <div className="loading-spinner">⏳ Đang tải...</div>}
        
        {!loading && filteredList.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📭</div>
            <p>{searchTerm ? "Không tìm thấy hoạt động" : "Chưa có hoạt động"}</p>
            <button className="btn-secondary" onClick={() => setShowModal(true)}>
              Thêm hoạt động đầu tiên
            </button>
          </div>
        ) : (
          !loading && (
            <table className="data-table">
              <thead>
                <tr>
                  <th className="col-index">STT</th>
                  <th className="col-code">Mã</th>
                  <th className="col-title">Tiêu đề</th>
                  <th className="col-subject">Môn học</th>
                  <th className="col-type">Loại</th>
                  <th className="col-score">Điểm</th>
                  <th className="col-action">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredList.map((hd, index) => (
                  <tr key={hd.maHoatDong} className="table-row">
                    <td className="col-index">{index + 1}</td>
                    <td className="col-code">
                      <span className="badge-code">{hd.maHoatDong}</span>
                    </td>
                    <td className="col-title">
                      <div className="activity-title">{hd.tieuDe}</div>
                    </td>
                    <td className="col-subject">
                      <span className="badge-subject">{hd.maMonHoc}</span>
                    </td>
                    <td className="col-type">
                      <span className="badge-type">{hd.maLoai}</span>
                    </td>
                    <td className="col-score">
                      <span className="score-badge">💰 {hd.tongDiemToiDa}</span>
                    </td>
                    <td className="col-action">
                      <div className="action-buttons">
                        <button 
                          className="btn-icon btn-edit"
                          onClick={() => handleEdit(hd)}
                          title="Chỉnh sửa"
                        >
                          ✏️
                        </button>
                        <button 
                          className="btn-icon btn-delete"
                          onClick={() => handleDelete(hd.maHoatDong)}
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

      {/* Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editingId ? "✏️ Chỉnh sửa hoạt động" : "➕ Thêm hoạt động mới"}</h2>
              <button className="btn-close" onClick={handleCloseModal}>✕</button>
            </div>

            <div className="modal-body">
              <div className="form-group">
                <label htmlFor="maHoatDong">Mã hoạt động *</label>
                <input
                  id="maHoatDong"
                  type="text"
                  placeholder="VD: HD001"
                  value={form.maHoatDong}
                  onChange={e => setForm({ ...form, maHoatDong: e.target.value })}
                  className="form-input"
                  disabled={editingId !== null}
                />
              </div>

              <div className="form-group">
                <label htmlFor="tieuDe">Tiêu đề *</label>
                <input
                  id="tieuDe"
                  type="text"
                  placeholder="VD: Kiểm tra giữa kỳ"
                  value={form.tieuDe}
                  onChange={e => setForm({ ...form, tieuDe: e.target.value })}
                  className="form-input"
                />
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="maMonHoc">Mã môn học</label>
                  <input
                    id="maMonHoc"
                    type="text"
                    placeholder="VD: TOAN"
                    value={form.maMonHoc}
                    onChange={e => setForm({ ...form, maMonHoc: e.target.value })}
                    className="form-input"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="maLoai">Mã loại</label>
                  <input
                    id="maLoai"
                    type="text"
                    placeholder="VD: BT, KT"
                    value={form.maLoai}
                    onChange={e => setForm({ ...form, maLoai: e.target.value })}
                    className="form-input"
                  />
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="tongDiemToiDa">Tổng điểm tối đa</label>
                <input
                  id="tongDiemToiDa"
                  type="number"
                  min="0"
                  placeholder="0"
                  value={form.tongDiemToiDa}
                  onChange={e => setForm({ ...form, tongDiemToiDa: e.target.value })}
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