import { useEffect, useState } from "react";
import { getDashboard, getDashboardCharts } from "../api/dashboardApi";
import "./Dashboard.css";

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [fromDate, setFromDate] = useState("2025-01-01");
  const [toDate, setToDate] = useState("2025-12-31");
  const [chartData, setChartData] = useState(null);

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

  useEffect(() => {
    const fetchCharts = async () => {
      try {
        const result = await getDashboardCharts(fromDate, toDate);
        setChartData(result);
      } catch (err) {
        console.error("Error loading charts:", err);
      }
    };

    fetchCharts();
  }, [fromDate, toDate]);

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

  const statsData = [
    { name: "Học sinh", value: data.tongHocSinh, icon: "👨‍🎓", color: "#3b82f6" },
    { name: "Môn học", value: data.tongMonHoc, icon: "📚", color: "#10b981" },
    { name: "Hoạt động", value: data.tongHoatDong, icon: "📝", color: "#f59e0b" },
    { name: "Câu hỏi", value: data.tongCauHoi, icon: "❓", color: "#8b5cf6" }
  ];

  const topStudents = data.topHocSinhTheoDiem?.slice(0, 10) || [];
  const avgScore = topStudents.length > 0
    ? Math.round(topStudents.reduce((sum, u) => sum + u.tongDiem, 0) / topStudents.length)
    : 0;
  const maxScore = topStudents[0]?.tongDiem || 0;

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

      {/* Time Filter */}
      <div className="time-filter">
        <div className="filter-item">
          <label>Từ ngày</label>
          <input
            type="date"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
          />
        </div>
        <div className="filter-item">
          <label>Đến ngày</label>
          <input
            type="date"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
          />
        </div>
      </div>

      {/* Charts Section */}
      <div className="charts-section">
        {/* Bar Chart - Top Students */}
        <div className="chart-card chart-full">
          <h3>🏆 Top 10 học sinh theo điểm</h3>
          <div className="bar-chart">
            {topStudents.map((student, index) => {
              const percentage = (student.tongDiem / maxScore) * 100;
              return (
                <div key={student.maNguoiDung} className="bar-item">
                  <div className="bar-label">
                    <span className="bar-rank">{index + 1}</span>
                    <span className="bar-name">{student.tenDangNhap}</span>
                  </div>
                  <div className="bar-container">
                    <div
                      className="bar-fill"
                      style={{
                        width: `${percentage}%`,
                        backgroundColor: `hsl(${200 - index * 15}, 70%, 50%)`
                      }}
                    >
                      <span className="bar-value">{student.tongDiem}</span>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Line Chart - Activity by Date */}
        {chartData && (
          <div className="chart-card chart-full">
            <h3>📅 Lượt học theo ngày</h3>
            <div className="line-chart">
              {chartData.theoNgay?.map((item) => (
                <div key={item.ngay} className="line-item">
                  <span className="line-label">{item.ngay}</span>
                  <div className="line-bar">
                    <div
                      className="line-fill"
                      style={{
                        width: `${Math.max(item.soLuot * 10, 2)}px`,
                        backgroundColor: "#3b82f6"
                      }}
                    >
                      <span className="line-value">{item.soLuot}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Distribution Chart - System Data */}
        <div className="chart-card chart-half">
          <h3>📈 Phân bố dữ liệu hệ thống</h3>
          <div className="distribution-chart">
            {statsData.map((stat, index) => {
              const percentage = (stat.value / Math.max(...statsData.map(s => s.value))) * 100;
              return (
                <div key={index} className="dist-item">
                  <div className="dist-info">
                    <span className="dist-name">{stat.name}</span>
                    <span className="dist-value">{stat.value}</span>
                  </div>
                  <div className="dist-bar">
                    <div
                      className="dist-fill"
                      style={{
                        width: `${percentage}%`,
                        backgroundColor: stat.color
                      }}
                    ></div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Activity by Type */}
        {chartData && (
          <div className="chart-card chart-half">
            <h3>🎯 Hoạt động theo loại</h3>
            <div className="distribution-chart">
              {chartData.theoLoai?.map((item) => (
                <div key={item.tenLoai} className="dist-item">
                  <div className="dist-info">
                    <span className="dist-name">{item.tenLoai}</span>
                    <span className="dist-value">{item.soLuot}</span>
                  </div>
                  <div className="dist-bar">
                    <div
                      className="dist-fill"
                      style={{
                        width: `${Math.max(item.soLuot * 10, 2)}px`,
                        backgroundColor: "#10b981"
                      }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Activity by Subject */}
        {chartData && (
          <div className="chart-card chart-half">
            <h3>📚 Hoạt động theo môn học</h3>
            <div className="distribution-chart">
              {chartData.theoMon?.map((item) => (
                <div key={item.tenMonHoc} className="dist-item">
                  <div className="dist-info">
                    <span className="dist-name">{item.tenMonHoc}</span>
                    <span className="dist-value">{item.soLuot}</span>
                  </div>
                  <div className="dist-bar">
                    <div
                      className="dist-fill"
                      style={{
                        width: `${Math.max(item.soLuot * 10, 2)}px`,
                        backgroundColor: "#f59e0b"
                      }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Leaderboard */}
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

      {/* Summary */}
      <div className="summary-card">
        <h3>📌 Thống kê tổng hợp</h3>
        <div className="summary-grid">
          <div className="summary-item">
            <p className="summary-label">Tổng người dùng</p>
            <p className="summary-number">{data.tongHocSinh}</p>
          </div>
          <div className="summary-item">
            <p className="summary-label">Trung bình điểm</p>
            <p className="summary-number">{avgScore}</p>
          </div>
          <div className="summary-item">
            <p className="summary-label">Điểm cao nhất</p>
            <p className="summary-number">{maxScore}</p>
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