import { useEffect, useState } from "react";
import { getMonHoc, createMonHoc, deleteMonHoc } from "../api/monHocApi";

export default function MonHoc() {
  const [list, setList] = useState([]);
  const [maMonHoc, setMaMonHoc] = useState("");
  const [tenMonHoc, setTenMonHoc] = useState("");

  const loadData = async () => {
    const data = await getMonHoc();
    setList(data);
  };

  useEffect(() => {
    const fetchData = async () => {
      const data = await getMonHoc();
      setList(data);
    };
  
    fetchData();
  }, []);

  const handleCreate = async () => {
    if (!maMonHoc || !tenMonHoc) {
      alert("Nhập đầy đủ thông tin");
      return;
    }

    await createMonHoc({
      maMonHoc,
      tenMonHoc
    });

    setMaMonHoc("");
    setTenMonHoc("");
    loadData();
  };

  const handleDelete = async (ma) => {
    if (window.confirm("Xóa môn học này?")) {
      await deleteMonHoc(ma);
      loadData();
    }
  };

  return (
    <div>
      <h2>📚 Quản lý môn học</h2>

      {/* FORM THÊM */}
      <div style={{ marginBottom: 20 }}>
        <input
          placeholder="Mã môn"
          value={maMonHoc}
          onChange={e => setMaMonHoc(e.target.value)}
        />
        <input
          placeholder="Tên môn"
          value={tenMonHoc}
          onChange={e => setTenMonHoc(e.target.value)}
        />
        <button onClick={handleCreate}>Thêm môn</button>
      </div>

      {/* TABLE */}
      <table border="1" cellPadding="6">
        <thead>
          <tr>
            <th>Mã môn</th>
            <th>Tên môn</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {list.map(m => (
            <tr key={m.maMonHoc}>
              <td>{m.maMonHoc}</td>
              <td>{m.tenMonHoc}</td>
              <td>
                <button onClick={() => handleDelete(m.maMonHoc)}>
                  Xóa
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
