import { useEffect, useState } from "react";
import {
  getCauHoi,
  createCauHoi,
  deleteCauHoi,
  getDapAn,
  createDapAn,
  deleteDapAn
} from "../api/cauHoiApi";

export default function CauHoi() {
  const [list, setList] = useState([]);
  const [selected, setSelected] = useState(null);

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
    setList(await getCauHoi());
  };

  useEffect(() => {
      const fetchData = async () => {
        const data = await getCauHoi();
        setList(data);
      };
    
      fetchData();
    }, []);

  const submitCauHoi = async () => {
    if (!ch.maCauHoi || !ch.noiDungCauHoi) {
      alert("Thiếu dữ liệu câu hỏi");
      return;
    }
    await createCauHoi(ch);
    setCh({ maCauHoi: "", noiDungCauHoi: "", diemToiDa: 1 });
    loadCauHoi();
  };

  const removeCauHoi = async (id) => {
    if (window.confirm("Xóa câu hỏi?")) {
      await deleteCauHoi(id);
      setSelected(null);
      loadCauHoi();
    }
  };

  const selectCauHoi = async (cauHoi) => {
    setSelected(cauHoi);
    setDapAnList(await getDapAn(cauHoi.maCauHoi));
  };

  const submitDapAn = async () => {
    if (!selected) return;

    await createDapAn(selected.maCauHoi, da);
    setDa({ maDapAn: "", noiDungDapAn: "", laDapAnDung: false });
    setDapAnList(await getDapAn(selected.maCauHoi));
  };

  const removeDapAn = async (id) => {
    await deleteDapAn(id);
    setDapAnList(await getDapAn(selected.maCauHoi));
  };

  return (
    <div>
      <h2>❓ Quản lý câu hỏi & đáp án</h2>

      {/* FORM CÂU HỎI */}
      <h4>Thêm câu hỏi</h4>
      <input
        placeholder="Mã câu hỏi"
        value={ch.maCauHoi}
        onChange={e => setCh({ ...ch, maCauHoi: e.target.value })}
      />
      <input
        placeholder="Nội dung"
        value={ch.noiDungCauHoi}
        onChange={e => setCh({ ...ch, noiDungCauHoi: e.target.value })}
      />
      <input
        type="number"
        placeholder="Điểm"
        value={ch.diemToiDa}
        onChange={e => setCh({ ...ch, diemToiDa: e.target.value })}
      />
      <button onClick={submitCauHoi}>Thêm câu hỏi</button>

      <hr />

      {/* DANH SÁCH CÂU HỎI */}
      <h4>Danh sách câu hỏi</h4>
      <ul>
        {list.map(c => (
          <li key={c.maCauHoi}>
            <span
              style={{ cursor: "pointer", fontWeight: selected?.maCauHoi === c.maCauHoi ? "bold" : "normal" }}
              onClick={() => selectCauHoi(c)}
            >
              {c.maCauHoi} - {c.noiDungCauHoi}
            </span>
            <button onClick={() => removeCauHoi(c.maCauHoi)}>Xóa</button>
          </li>
        ))}
      </ul>

      {/* ĐÁP ÁN */}
      {selected && (
        <>
          <hr />
          <h4>Đáp án cho: {selected.noiDungCauHoi}</h4>

          <input
            placeholder="Mã đáp án"
            value={da.maDapAn}
            onChange={e => setDa({ ...da, maDapAn: e.target.value })}
          />
          <input
            placeholder="Nội dung đáp án"
            value={da.noiDungDapAn}
            onChange={e => setDa({ ...da, noiDungDapAn: e.target.value })}
          />
          <label>
            <input
              type="checkbox"
              checked={da.laDapAnDung}
              onChange={e => setDa({ ...da, laDapAnDung: e.target.checked })}
            />
            Đáp án đúng
          </label>
          <button onClick={submitDapAn}>Thêm đáp án</button>

          <ul>
            {dapAnList.map(d => (
              <li key={d.maDapAn}>
                {d.noiDungDapAn}
                {d.laDapAnDung && " ✅"}
                <button onClick={() => removeDapAn(d.maDapAn)}>Xóa</button>
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );
}
