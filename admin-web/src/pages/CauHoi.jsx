import { useEffect, useState } from "react";
import {
  getCauHoi,
  createCauHoi,
  deleteCauHoi,
  getDapAn,
  createDapAn,
  deleteDapAn
} from "../api/cauHoiApi";
import "./CauHoi.css";

export default function CauHoi() {
  const [list, setList] = useState([]);
  const [selected, setSelected] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showCauHoiModal, setShowCauHoiModal] = useState(false);
  const [showDapAnModal, setShowDapAnModal] = useState(false);

  // form câu hỏi
  const [ch, setCh] = useState({
    maCauHoi: "",
    noiDungCauHoi: "",
    diemToiDa: 1
  });

  // form đáp án
  const [da, setDa] = useState({
    maDapAn: "",
    noiDungDapAn: "",
    laDapAnDung: false
  });

  const [dapAnList, setDapAnList] = useState([]);

  const loadCauHoi = async () => {
    setLoading(true);
    try {
      const data = await getCauHoi();
      setList(data);
    } catch (error) {
      console.error("Error:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCauHoi();
  }, []);

  const submitCauHoi = async () => {
    if (!ch.maCauHoi.trim() || !ch.noiDungCauHoi.trim()) {
      alert("Vui lòng nhập đầy đủ thông tin câu hỏi");
      return;
    }
    setLoading(true);
    try {
      await createCauHoi(ch);
      setCh({ maCauHoi: "", noiDungCauHoi: "", diemToiDa: 1 });
      setShowCauHoiModal(false);
      loadCauHoi();
    } catch (error) {
      alert("Lỗi: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const removeCauHoi = async (id) => {
    if (window.confirm("Bạn chắc chắn muốn xóa câu hỏi này?")) {
      setLoading(true);
      try {
        await deleteCauHoi(id);
        if (selected?.maCauHoi === id) {
          setSelected(null);
          setDapAnList([]);
        }
        loadCauHoi();
      } catch (error) {
        alert("Lỗi xóa: " + error.message);
      } finally {
        setLoading(false);
      }
    }
  };

  const selectCauHoi = async (cauHoi) => {
    setSelected(cauHoi);
    setDapAnList([]);
    setDa({ maDapAn: "", noiDungDapAn: "", laDapAnDung: false });
    try {
      const dapAns = await getDapAn(cauHoi.maCauHoi);
      setDapAnList(dapAns);
    } catch (error) {
      console.error("Error loading answers:", error);
    }
  };

  const submitDapAn = async () => {
    if (!selected) return;
    if (!da.maDapAn.trim() || !da.noiDungDapAn.trim()) {
      alert("Vui lòng nhập đầy đủ thông tin đáp án");
      return;
    }

    setLoading(true);
    try {
      await createDapAn(selected.maCauHoi, da);
      setDa({ maDapAn: "", noiDungDapAn: "", laDapAnDung: false });
      setShowDapAnModal(false);
      const dapAns = await getDapAn(selected.maCauHoi);
      setDapAnList(dapAns);
    } catch (error) {
      alert("Lỗi: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const removeDapAn = async (id) => {
    if (window.confirm("Bạn chắc chắn muốn xóa đáp án này?")) {
      setLoading(true);
      try {
        await deleteDapAn(id);
        const dapAns = await getDapAn(selected.maCauHoi);
        setDapAnList(dapAns);
      } catch (error) {
        alert("Lỗi xóa: " + error.message);
      } finally {
        setLoading(false);
      }
    }
  };

  const handleCloseCauHoiModal = () => {
    setCh({ maCauHoi: "", noiDungCauHoi: "", diemToiDa: 1 });
    setShowCauHoiModal(false);
  };

  const handleCloseDapAnModal = () => {
    setDa({ maDapAn: "", noiDungDapAn: "", laDapAnDung: false });
    setShowDapAnModal(false);
  };

  const correctAnswersCount = dapAnList.filter(d => d.laDapAnDung).length;

  return (
    <div className="cau-hoi-container">
      {/* Header */}
      <div className="page-header">
        <div className="header-left">
          <h1>❓ Quản lý câu hỏi & đáp án</h1>
          <p className="subtitle">Tổng cộng: <span className="badge-count">{list.length}</span> câu hỏi</p>
        </div>
        <button 
          className="btn-primary btn-lg"
          onClick={() => setShowCauHoiModal(true)}
        >
          + Thêm câu hỏi
        </button>
      </div>

      {/* Main Content */}
      <div className="content-wrapper">
        {/* Questions List */}
        <div className="questions-panel">
          <div className="panel-header">
            <h3>📋 Danh sách câu hỏi</h3>
          </div>

          {loading && list.length === 0 ? (
            <div className="loading">⏳ Đang tải...</div>
          ) : list.length === 0 ? (
            <div className="empty-list">
              <p>📭 Chưa có câu hỏi</p>
            </div>
          ) : (
            <div className="questions-list">
              {list.map((c, index) => (
                <div
                  key={c.maCauHoi}
                  className={`question-item ${selected?.maCauHoi === c.maCauHoi ? "active" : ""}`}
                  onClick={() => selectCauHoi(c)}
                >
                  <div className="question-content">
                    <div className="question-badge">{index + 1}</div>
                    <div className="question-text">
                      <p className="question-code">{c.maCauHoi}</p>
                      <p className="question-content-text">{c.noiDungCauHoi}</p>
                      <span className="question-score">💰 {c.diemToiDa} điểm</span>
                    </div>
                  </div>
                  <button
                    className="btn-delete-small"
                    onClick={(e) => {
                      e.stopPropagation();
                      removeCauHoi(c.maCauHoi);
                    }}
                    title="Xóa câu hỏi"
                  >
                    🗑️
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Answers Panel */}
        <div className="answers-panel">
          {selected ? (
            <>
              <div className="panel-header">
                <h3>📝 Đáp án</h3>
                <button
                  className="btn-primary"
                  onClick={() => setShowDapAnModal(true)}
                >
                  + Thêm đáp án
                </button>
              </div>

              <div className="selected-question">
                <div className="q-info">
                  <strong>Câu hỏi:</strong>
                  <p>{selected.noiDungCauHoi}</p>
                </div>
                <div className="q-score">
                  <strong>Điểm:</strong>
                  <span>{selected.diemToiDa}</span>
                </div>
              </div>

              {dapAnList.length === 0 ? (
                <div className="empty-answers">
                  <p>📭 Chưa có đáp án</p>
                  <button
                    className="btn-secondary"
                    onClick={() => setShowDapAnModal(true)}
                  >
                    Thêm đáp án đầu tiên
                  </button>
                </div>
              ) : (
                <div className="answers-list">
                  <div className="answers-info">
                    Tổng: <strong>{dapAnList.length}</strong> đáp án
                    {correctAnswersCount > 0 && (
                      <span className="correct-count">✅ {correctAnswersCount} đúng</span>
                    )}
                  </div>
                  {dapAnList.map((d) => (
                    <div key={d.maDapAn} className={`answer-item ${d.laDapAnDung ? "correct" : ""}`}>
                      <div className="answer-content">
                        <div className="answer-text">
                          <p className="answer-code">{d.maDapAn}</p>
                          <p className="answer-text-content">{d.noiDungDapAn}</p>
                        </div>
                        {d.laDapAnDung && <span className="correct-badge">✅ Đáp án đúng</span>}
                      </div>
                      <button
                        className="btn-delete-small"
                        onClick={() => removeDapAn(d.maDapAn)}
                        title="Xóa đáp án"
                      >
                        🗑️
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </>
          ) : (
            <div className="empty-panel">
              <div className="empty-icon">👈</div>
              <p>Chọn một câu hỏi để xem đáp án</p>
            </div>
          )}
        </div>
      </div>

      {/* Modal Thêm Câu Hỏi */}
      {showCauHoiModal && (
        <div className="modal-overlay" onClick={handleCloseCauHoiModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>➕ Thêm câu hỏi mới</h2>
              <button className="btn-close" onClick={handleCloseCauHoiModal}>✕</button>
            </div>

            <div className="modal-body">
              <div className="form-group">
                <label>Mã câu hỏi *</label>
                <input
                  type="text"
                  placeholder="VD: CH001"
                  value={ch.maCauHoi}
                  onChange={e => setCh({ ...ch, maCauHoi: e.target.value })}
                  className="form-input"
                />
              </div>

              <div className="form-group">
                <label>Nội dung câu hỏi *</label>
                <textarea
                  placeholder="Nhập nội dung câu hỏi..."
                  value={ch.noiDungCauHoi}
                  onChange={e => setCh({ ...ch, noiDungCauHoi: e.target.value })}
                  className="form-input"
                  rows="4"
                />
              </div>

              <div className="form-group">
                <label>Điểm tối đa</label>
                <input
                  type="number"
                  min="1"
                  value={ch.diemToiDa}
                  onChange={e => setCh({ ...ch, diemToiDa: e.target.value })}
                  className="form-input"
                />
              </div>
            </div>

            <div className="modal-footer">
              <button className="btn-secondary" onClick={handleCloseCauHoiModal}>
                Hủy
              </button>
              <button
                className="btn-primary"
                onClick={submitCauHoi}
                disabled={loading}
              >
                {loading ? "Đang xử lý..." : "Thêm câu hỏi"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal Thêm Đáp Án */}
      {showDapAnModal && (
        <div className="modal-overlay" onClick={handleCloseDapAnModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>➕ Thêm đáp án mới</h2>
              <button className="btn-close" onClick={handleCloseDapAnModal}>✕</button>
            </div>

            <div className="modal-body">
              <div className="form-group">
                <label>Mã đáp án *</label>
                <input
                  type="text"
                  placeholder="VD: DA001"
                  value={da.maDapAn}
                  onChange={e => setDa({ ...da, maDapAn: e.target.value })}
                  className="form-input"
                />
              </div>

              <div className="form-group">
                <label>Nội dung đáp án *</label>
                <textarea
                  placeholder="Nhập nội dung đáp án..."
                  value={da.noiDungDapAn}
                  onChange={e => setDa({ ...da, noiDungDapAn: e.target.value })}
                  className="form-input"
                  rows="3"
                />
              </div>

              <div className="form-group checkbox">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={da.laDapAnDung}
                    onChange={e => setDa({ ...da, laDapAnDung: e.target.checked })}
                  />
                  <span>✅ Đây là đáp án đúng</span>
                </label>
              </div>
            </div>

            <div className="modal-footer">
              <button className="btn-secondary" onClick={handleCloseDapAnModal}>
                Hủy
              </button>
              <button
                className="btn-primary"
                onClick={submitDapAn}
                disabled={loading}
              >
                {loading ? "Đang xử lý..." : "Thêm đáp án"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}