import { useEffect, useState } from "react";
import { getDashboard } from "../api/dashboardApi";

export default function Dashboard() {
  const [data, setData] = useState(null);

  useEffect(() => {
    getDashboard().then(setData);
  }, []);

  if (!data) return <p>Loading...</p>;

  return (
    <div style={{ padding: 30 }}>
      <h1>Admin Dashboard</h1>

      <p>👨‍🎓 Học sinh: {data.tongHocSinh}</p>
      <p>📚 Môn học: {data.tongMonHoc}</p>
      <p>📝 Hoạt động: {data.tongHoatDong}</p>
      <p>❓ Câu hỏi: {data.tongCauHoi}</p>

      <h3>🏆 Top học sinh theo điểm</h3>
      <ul>
        {data.topHocSinhTheoDiem.map((u) => (
          <li key={u.maNguoiDung}>
            {u.tenDangNhap} - {u.tongDiem} điểm
          </li>
        ))}
      </ul>
    </div>
  );
}
