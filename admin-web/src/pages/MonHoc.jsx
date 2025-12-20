import { useEffect, useState } from "react";
import { getMonHoc, createMonHoc, deleteMonHoc } from "../api/monHocApi";
import "./MonHoc.css";

export default function MonHoc() {
  const [list, setList] = useState([]);
  const [maMonHoc, setMaMonHoc] = useState("");
  const [tenMonHoc, setTenMonHoc] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const loadData = async () => {
    setLoading(true);
    try {
      const data = await getMonHoc();
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

  const handleCreate = async () => {
    if (!maMonHoc.trim() || !tenMonHoc.trim()) {
      alert("Vui lòng nhập đầy đủ thông tin");
      return;
    }

    setLoading(true);
    try {
      await createMonHoc({
        maMonHoc,
        tenMonHoc
      });
      
      setMaMonHoc("");
      setTenMonHoc("");
      setShowModal(false);
      loadData();
    } catch (error) {
      alert("Lỗi: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (ma) => {
    if (window.confirm("Bạn chắc chắn muốn xóa môn học này?")) {
      setLoading(true);
      try {
        await deleteMonHoc(ma);
        loadData();
      } catch (error) {
        alert("Lỗi xóa: " + error.message);
      } finally {
        setLoading(false);
      }
    }
  };

  const handleCloseModal = () => {
    setMaMonHoc("");
    setTenMonHoc("");
    setShowModal(false);
  };

  const filteredList = list.filter(m =>
    m.maMonHoc.toLowerCase().includes(searchTerm.toLowerCase()) ||
    m.tenMonHoc.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="mon-hoc-container">
      {/* Header Section */}
      <div className="page-header">
        <div className="header-left">
          <h1>📚 Quản lý môn học</h1>
          <p className="subtitle">Tổng cộng: <span className="badge-count">{list.length}</span> môn học</p>
        </div>
        <button 
          className="btn-primary btn-lg"
          onClick={() => setShowModal(true)}
        >
          + Thêm môn học
        </button>
      </div>

      {/* Search Bar */}
      <div className="search-section">
        <div className="search-box">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Tìm kiếm theo mã hoặc tên môn..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>
        <p className="result-count">Kết quả: {filteredList.length} môn học</p>
      </div>

      {/* Table Section */}
      <div className="table-container">
        {loading && <div className="loading-spinner">⏳ Đang tải...</div>}
        
        {!loading && filteredList.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📭</div>
            <p>Không có dữ liệu</p>
            <button className="btn-secondary" onClick={() => setShowModal(true)}>
              Thêm môn học đầu tiên
            </button>
          </div>
        ) : (
          !loading && (
            <table className="data-table">
              <thead>
                <tr>
                  <th className="col-index">STT</th>
                  <th className="col-code">Mã môn</th>
                  <th className="col-name">Tên môn học</th>
                  <th className="col-action">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredList.map((m, index) => (
                  <tr key={m.maMonHoc} className="table-row">
                    <td className="col-index">{index + 1}</td>
                    <td className="col-code">
                      <span className="badge-code">{m.maMonHoc}</span>
                    </td>
                    <td className="col-name">
                      <div className="subject-name">{m.tenMonHoc}</div>
                    </td>
                    <td className="col-action">
                      <div className="action-buttons">
                        <button 
                          className="btn-icon btn-delete"
                          onClick={() => handleDelete(m.maMonHoc)}
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
              <h2>➕ Thêm môn học mới</h2>
              <button className="btn-close" onClick={handleCloseModal}>✕</button>
            </div>

            <div className="modal-body">
              <div className="form-group">
                <label htmlFor="maMonHoc">Mã môn học *</label>
                <input
                  id="maMonHoc"
                  type="text"
                  placeholder="VD: MH001"
                  value={maMonHoc}
                  onChange={e => setMaMonHoc(e.target.value)}
                  className="form-input"
                />
              </div>

              <div className="form-group">
                <label htmlFor="tenMonHoc">Tên môn học *</label>
                <input
                  id="tenMonHoc"
                  type="text"
                  placeholder="VD: Toán học"
                  value={tenMonHoc}
                  onChange={e => setTenMonHoc(e.target.value)}
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
                onClick={handleCreate}
                disabled={loading}
              >
                {loading ? "Đang xử lý..." : "Thêm"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}