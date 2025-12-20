import { useEffect, useState } from "react";
import { getDashboard } from "../api/dashboardApi";
import { BarChart, Bar, LineChart, Line, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";
import "./Dashboard.css";

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const result = await getDashboard();
        setData(result);
      } catch (error) {
        console.error("Error loading dashboard:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">⏳</div>
        <p>Đang tải dữ liệu...</p>
      </div>
    );
  }

  if (!data) {
    return (
      <div className="error-container">
        <p>❌ Lỗi tải dữ liệu</p>
      </div>
    );
  }

  // Chuẩn bị dữ liệu cho biểu đồ Top học sinh
  const topStudentsChartData = data.topHocSinhTheoDiem?.slice(0, 10).map(u => ({
    name: u.tenDangNhap,
    điểm: u.tongDiem
  })) || [];

  // Dữ liệu cho biểu đồ tổng quan
  const statsData = [
    { name: "Học sinh", value: data.tongHocSinh, icon: "👨‍🎓", color: "#3b82f6" },
    { name: "Môn học", value: data.tongMonHoc, icon: "📚", color: "#10b981" },
    { name: "Hoạt động", value: data.tongHoatDong, icon: "📝", color: "#f59e0b" },
    { name: "Câu hỏi", value: data.tongCauHoi, icon: "❓", color: "#8b5cf6" }
  ];

  // Dữ liệu cho biểu đồ tròn
  const pieData = [
    { name: "Học sinh", value: data.tongHocSinh },
    { name: "Môn học", value: data.tongMonHoc },
    { name: "Hoạt động", value: data.tongHoatDong },
    { name: "Câu hỏi", value: data.tongCauHoi }
  ];

  const COLORS = ["#3b82f6", "#10b981", "#f59e0b", "#8b5cf6"];

  return (
    <div className="dashboard-container">
      {/* Header */}
      <div className="dashboard-header">
        <h1>📊 Bảng điều khiển quản trị</h1>
        <p className="dashboard-subtitle">Thống kê tổng hợp hệ thống học tập</p>
      </div>

      {/* Stats Cards */}
      <div className="stats-grid">
        {statsData.map((stat, index) => (
          <div key={index} className="stat-card">
            <div className="stat-icon">{stat.icon}</div>
            <div className="stat-content">
              <p className="stat-label">{stat.name}</p>
              <p className="stat-value">{stat.value}</p>
            </div>
            <div className="stat-bar" style={{ backgroundColor: stat.color }}></div>
          </div>
        ))}
      </div>

      {/* Charts Grid */}
      <div className="charts-grid">
        {/* Top Students Bar Chart */}
        <div className="chart-card">
          <h3>🏆 Top 10 học sinh theo điểm</h3>
          <div className="chart-container">
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={topStudentsChartData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                <XAxis dataKey="name" angle={-45} textAnchor="end" height={100} tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip 
                  contentStyle={{ backgroundColor: "#1e293b", border: "1px solid #e2e8f0", borderRadius: 6 }}
                  labelStyle={{ color: "#fff" }}
                  formatter={(value) => [`${value} điểm`, "Điểm"]}
                />
                <Bar dataKey="điểm" fill="#3b82f6" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Overview Pie Chart */}
        <div className="chart-card">
          <h3>📈 Phân bố dữ liệu hệ thống</h3>
          <div className="chart-container">
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={pieData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, value }) => `${name}: ${value}`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {COLORS.map((color, index) => (
                    <Cell key={`cell-${index}`} fill={color} />
                  ))}
                </Pie>
                <Tooltip 
                  contentStyle={{ backgroundColor: "#1e293b", border: "1px solid #e2e8f0", borderRadius: 6 }}
                  labelStyle={{ color: "#fff" }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Top Students Leaderboard */}
      <div className="leaderboard-card">
        <h3>🎖️ Bảng xếp hạng học sinh</h3>
        <div className="leaderboard-table">
          <table className="rank-table">
            <thead>
              <tr>
                <th className="rank-col">Xếp hạng</th>
                <th className="name-col">Tên đăng nhập</th>
                <th className="score-col">Điểm số</th>
                <th className="badge-col">Huy hiệu</th>
              </tr>
            </thead>
            <tbody>
              {data.topHocSinhTheoDiem?.map((user, index) => (
                <tr key={user.maNguoiDung} className="rank-row">
                  <td className="rank-col">
                    <span className={`rank-badge rank-${index + 1}`}>
                      {index === 0 ? "🥇" : index === 1 ? "🥈" : index === 2 ? "🥉" : index + 1}
                    </span>
                  </td>
                  <td className="name-col">
                    <span className="user-name">{user.tenDangNhap}</span>
                  </td>
                  <td className="score-col">
                    <span className="score-value">⭐ {user.tongDiem}</span>
                  </td>
                  <td className="badge-col">
                    {index === 0 ? "🌟 Xuất sắc" : index < 3 ? "⭐ Tốt" : index < 5 ? "✨ Khá" : "👍 Tiến bộ"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Statistics Summary */}
      <div className="summary-card">
        <h3>📌 Thống kê tổng hợp</h3>
        <div className="summary-grid">
          <div className="summary-item">
            <p className="summary-label">Tổng người dùng</p>
            <p className="summary-number">{data.tongHocSinh}</p>
          </div>
          <div className="summary-item">
            <p className="summary-label">Trung bình điểm</p>
            <p className="summary-number">
              {data.topHocSinhTheoDiem?.length > 0
                ? Math.round(
                    data.topHocSinhTheoDiem.reduce((sum, u) => sum + u.tongDiem, 0) /
                      data.topHocSinhTheoDiem.length
                  )
                : 0}
            </p>
          </div>
          <div className="summary-item">
            <p className="summary-label">Điểm cao nhất</p>
            <p className="summary-number">
              {data.topHocSinhTheoDiem?.[0]?.tongDiem || 0}
            </p>
          </div>
          <div className="summary-item">
            <p className="summary-label">Tổng hoạt động</p>
            <p className="summary-number">{data.tongHoatDong}</p>
          </div>
        </div>
      </div>
    </div>
  );
}