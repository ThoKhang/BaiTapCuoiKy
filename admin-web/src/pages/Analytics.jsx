import { useEffect, useState } from "react";
import {
  getAnalyticsOverview,
  getAnalyticsActivities,
  getAnalyticsStudents
} from "../api/analyticsApi";
import jsPDF from "jspdf";
import "../fonts/Roboto-Regular-normal";
import "./Analytics.css";

export default function Analytics() {
  const [fromDate, setFromDate] = useState("2024-01-01");
  const [toDate, setToDate] = useState("2025-12-31");

  const [overview, setOverview] = useState(null);
  const [activities, setActivities] = useState(null);
  const [students, setStudents] = useState(null);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAnalytics = async () => {
      setLoading(true);
      try {
        const ov = await getAnalyticsOverview(fromDate, toDate);
        const act = await getAnalyticsActivities(fromDate, toDate);
        const stu = await getAnalyticsStudents(fromDate, toDate);

        setOverview(ov.overview);
        setActivities(act);
        setStudents(stu.topStudents);
      } catch (err) {
        console.error("Error loading analytics", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAnalytics();
  }, [fromDate, toDate]);

  const handleExportPDF = () => {
    try {
      console.log("Starting PDF export...");
      
      // Function to clean text
      const cleanText = (text) => text?.toString() || "";

      
      const doc = new jsPDF();
doc.setFont("Roboto-Regular", "normal");
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    let yPosition = 15;

    // Header
    doc.setFontSize(20);
    doc.setTextColor(15, 23, 42);
    doc.text("BÁO CÁO PHÂN TÍCH CHUYÊN SÂU \nAPP HỌC TẬP DÀNH CHO TRẺ", pageWidth / 2, yPosition, {
      align: "center"
    });
    yPosition += 8;

    // Date range
    doc.setFontSize(10);
    doc.setTextColor(100, 116, 139);
    doc.text(
      `\nKhoảng thời gian: ${fromDate} đến ${toDate}`,
      pageWidth / 2,
      yPosition,
      { align: "center" }
    );
    yPosition += 12;

    // KPI Section
    doc.setFontSize(12);
    doc.setTextColor(15, 23, 42);
    doc.text("TỔNG QUAN KPI", 14, yPosition);
    yPosition += 8;

    // KPI Table
    const col = [60, 60];
    const startX = 14;
    const startY = yPosition;
    
    // Header
    doc.setFillColor(59, 130, 246);
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(11);
    doc.setFont("Roboto-Regular", "normal");
    
    doc.rect(startX, startY, col[0], 8, "F");
    doc.text("Chỉ số", startX + 5, startY + 5);
    doc.rect(startX + col[0], startY, col[1], 8, "F");
    doc.text("Giá trị", startX + col[0] + 5, startY + 5);
    
    yPosition += 8;
    
    // Body
    doc.setTextColor(51, 65, 85);
    doc.setFont("Roboto-Regular", "normal");
    doc.setFontSize(10);
    
    const kpiRows = [
      ["Tổng lượt học", overview?.tongLuotThamGia?.toString() || "0"],
      ["Tỷ lệ hoàn thành", `${overview?.tyLeHoanThanh || 0}%`],
      ["Thời gian TB (phút)", overview?.thoiGianTBPhut?.toString() || "0"],
      ["Tổng điểm", overview?.tongDiem?.toString() || "0"]
    ];
    
    kpiRows.forEach((row, i) => {
      if (i % 2 === 0) doc.setFillColor(248, 250, 252);
      else doc.setFillColor(255, 255, 255);
      
      doc.rect(startX, yPosition, col[0], 7, "F");
      doc.rect(startX + col[0], yPosition, col[1], 7, "F");
      
      doc.text(row[0], startX + 5, yPosition + 4);
      doc.text(row[1], startX + col[0] + 5, yPosition + 4, { align: "right" });
      
      yPosition += 7;
    });
    
    yPosition += 8;

    // Activities Section
    if (activities?.top && activities.top.length > 0) {
      doc.setFontSize(12);
      doc.setTextColor(15, 23, 42);
      doc.setFont("Roboto-Regular", "normal");
      doc.text("HOẠT ĐỘNG ĐƯỢC THAM GIA NHIỀU NHẤT", 14, yPosition);
      yPosition += 8;

      doc.setFont("Roboto-Regular", "normal");
      doc.setFontSize(10);
      
      const cols = [20, 100, 50];
      const startXTable = 14;
      
      // Header
      doc.setFillColor(16, 185, 129);
      doc.setTextColor(255, 255, 255);
      doc.setFont("Roboto-Regular", "normal");

      
      doc.rect(startXTable, yPosition, cols[0], 8, "F");
      doc.text("#", startXTable + 3, yPosition + 5);
      doc.rect(startXTable + cols[0], yPosition, cols[1], 8, "F");
      doc.text("Hoạt động", startXTable + cols[0] + 5, yPosition + 5);
      doc.rect(startXTable + cols[0] + cols[1], yPosition, cols[2], 8, "F");
      doc.text("Lượt", startXTable + cols[0] + cols[1] + 5, yPosition + 5);
      
      yPosition += 8;
      
      doc.setFont("Roboto-Regular", "normal");
      doc.setTextColor(51, 65, 85);
      
      activities.top.forEach((a, i) => {
        if (i % 2 === 0) doc.setFillColor(248, 250, 252);
        else doc.setFillColor(255, 255, 255);
        
        doc.rect(startXTable, yPosition, cols[0], 7, "F");
        doc.rect(startXTable + cols[0], yPosition, cols[1], 7, "F");
        doc.rect(startXTable + cols[0] + cols[1], yPosition, cols[2], 7, "F");
        
        const title = cleanText(a.tieuDe).substring(0, 25);
        doc.text((i + 1).toString(), startXTable + 3, yPosition + 4);
        doc.text(title, startXTable + cols[0] + 5, yPosition + 4);
        doc.text(a.soLuot.toString(), startXTable + cols[0] + cols[1] + 5, yPosition + 4);
        
        yPosition += 7;
      });
      
      yPosition += 8;
    }

    // Least Popular Activities
    if (activities?.bottom && activities.bottom.length > 0) {
      doc.setFontSize(12);
      doc.setTextColor(15, 23, 42);
      doc.setFont("Roboto-Regular", "normal");

      doc.text("HOẠT ĐỘNG ÍT ĐƯỢC THAM GIA", 14, yPosition);
      yPosition += 8;

      doc.setFont("Roboto-Regular", "normal");
      doc.setFontSize(10);
      
      const cols = [20, 100, 50];
      const startXTable = 14;
      
      // Header
      doc.setFillColor(245, 158, 11);
      doc.setTextColor(255, 255, 255);
      doc.setFont("Roboto-Regular", "normal");

      
      doc.rect(startXTable, yPosition, cols[0], 8, "F");
      doc.text("#", startXTable + 3, yPosition + 5);
      doc.rect(startXTable + cols[0], yPosition, cols[1], 8, "F");
      doc.text("Hoạt động", startXTable + cols[0] + 5, yPosition + 5);
      doc.rect(startXTable + cols[0] + cols[1], yPosition, cols[2], 8, "F");
      doc.text("Lượt", startXTable + cols[0] + cols[1] + 5, yPosition + 5);
      
      yPosition += 8;
      
      doc.setFont("Roboto-Regular", "normal");
      doc.setTextColor(51, 65, 85);
      
      activities.bottom.forEach((a, i) => {
        if (i % 2 === 0) doc.setFillColor(248, 250, 252);
        else doc.setFillColor(255, 255, 255);
        
        doc.rect(startXTable, yPosition, cols[0], 7, "F");
        doc.rect(startXTable + cols[0], yPosition, cols[1], 7, "F");
        doc.rect(startXTable + cols[0] + cols[1], yPosition, cols[2], 7, "F");
        
        const title = cleanText(a.tieuDe).substring(0, 25);
        doc.text((i + 1).toString(), startXTable + 3, yPosition + 4);
        doc.text(title, startXTable + cols[0] + 5, yPosition + 4);
        doc.text(a.soLuot.toString(), startXTable + cols[0] + cols[1] + 5, yPosition + 4);
        
        yPosition += 7;
      });
      
      yPosition += 8;
    }

    // Students Section
    if (students && students.length > 0) {
      // Check if we need a new page
      if (yPosition > pageHeight - 60) {
        doc.addPage();
        yPosition = 15;
      }

      doc.setFontSize(12);
      doc.setTextColor(15, 23, 42);
      doc.setFont("Roboto-Regular", "normal");

      doc.text("HỌC SINH TÍCH CỰC NHẤT", 14, yPosition);
      yPosition += 8;

      doc.setFont("Roboto-Regular", "normal");
      doc.setFontSize(9);
      
      const cols = [15, 60, 25, 30, 30];
      const startXTable = 14;
      
      // Header
      doc.setFillColor(139, 92, 246);
      doc.setTextColor(255, 255, 255);
      doc.setFont("Roboto-Regular", "normal");

      
      doc.rect(startXTable, yPosition, cols[0], 8, "F");
      doc.text("#", startXTable + 2, yPosition + 5);
      doc.rect(startXTable + cols[0], yPosition, cols[1], 8, "F");
      doc.text("Tên đăng nhập", startXTable + cols[0] + 3, yPosition + 5);
      doc.rect(startXTable + cols[0] + cols[1], yPosition, cols[2], 8, "F");
      doc.text("Lượt", startXTable + cols[0] + cols[1] + 2, yPosition + 5);
      doc.rect(startXTable + cols[0] + cols[1] + cols[2], yPosition, cols[3], 8, "F");
      doc.text("Hoàn thành", startXTable + cols[0] + cols[1] + cols[2] + 2, yPosition + 5);
      doc.rect(startXTable + cols[0] + cols[1] + cols[2] + cols[3], yPosition, cols[4], 8, "F");
      doc.text("Điểm", startXTable + cols[0] + cols[1] + cols[2] + cols[3] + 2, yPosition + 5);
      
      yPosition += 8;
      
      doc.setFont("Roboto-Regular", "normal");
      doc.setTextColor(51, 65, 85);
      
      students.forEach((s, i) => {
        if (i % 2 === 0) doc.setFillColor(248, 250, 252);
        else doc.setFillColor(255, 255, 255);
        
        doc.rect(startXTable, yPosition, cols[0], 7, "F");
        doc.rect(startXTable + cols[0], yPosition, cols[1], 7, "F");
        doc.rect(startXTable + cols[0] + cols[1], yPosition, cols[2], 7, "F");
        doc.rect(startXTable + cols[0] + cols[1] + cols[2], yPosition, cols[3], 7, "F");
        doc.rect(startXTable + cols[0] + cols[1] + cols[2] + cols[3], yPosition, cols[4], 7, "F");
        
        const name = cleanText(s.tenDangNhap).substring(0, 12);
        doc.text((i + 1).toString(), startXTable + 2, yPosition + 4);
        doc.text(name, startXTable + cols[0] + 3, yPosition + 4);
        doc.text(s.soLuot.toString(), startXTable + cols[0] + cols[1] + 2, yPosition + 4);
        doc.text(s.soHoanThanh.toString(), startXTable + cols[0] + cols[1] + cols[2] + 2, yPosition + 4);
        doc.text(s.tongDiem.toString(), startXTable + cols[0] + cols[1] + cols[2] + cols[3] + 2, yPosition + 4);
        
        yPosition += 7;
      });
    }

    // Footer
    const totalPages = doc.getNumberOfPages();
    for (let i = 1; i <= totalPages; i++) {
      doc.setPage(i);
      doc.setFontSize(9);
      doc.setTextColor(149, 165, 166);
      doc.text(
        `Trang ${i} / ${totalPages}`,
        pageWidth / 2,
        pageHeight - 10,
        { align: "center" }
      );
      doc.text(
        `Sinh ra lúc: ${new Date().toLocaleString("vi-VN")}`,
        pageWidth / 2,
        pageHeight - 5,
        { align: "center" }
      );
    }

    // Save PDF
    const filename = `Bao-cao-phan-tich-app-${fromDate}_${toDate}.pdf`;
    doc.save(filename);
    console.log("PDF exported successfully!");
    } catch (error) {
      console.error("Error exporting PDF:", error);
      alert("Lỗi xuất báo cáo: " + error.message);
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">⏳</div>
        <p>Dang phan tich du lieu...</p>
      </div>
    );
  }

  return (
    <div className="analytics-container">
      {/* Header */}
      <div className="analytics-header">
        <div className="header-content">
  <div style={{ display: 'flex', flexDirection: 'column' }}>
    <h1>📊 Phân tích chuyên sâu</h1>
    <p>Phân tích chi tiết hành vi và hiệu suất học tập của Ứng Dụng Học Tập Dành Cho Trẻ</p>
  </div>
</div>
        <button className="btn-export-pdf" onClick={handleExportPDF}>
          📥 Xuất báo cáo PDF
        </button>
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
        <div className="filter-info">
          📌 Hiển thị dữ liệu từ <span>{fromDate}</span> đến <span>{toDate}</span>
        </div>
      </div>

      {/* OVERVIEW KPI */}
      <div className="analytics-kpi">
        <div className="kpi-card kpi-visits">
          <div className="kpi-icon">👥</div>
          <div className="kpi-content">
            <p className="kpi-label">Tổng lượt học</p>
            <h3 className="kpi-value">{overview?.tongLuotThamGia || 0}</h3>
          </div>
          <div className="kpi-bar"></div>
        </div>

        <div className="kpi-card kpi-completion">
          <div className="kpi-icon">✅</div>
          <div className="kpi-content">
            <p className="kpi-label">Tỷ lệ hoàn thành</p>
            <h3 className="kpi-value">{overview?.tyLeHoanThanh || 0}%</h3>
          </div>
          <div className="kpi-bar"></div>
        </div>

        <div className="kpi-card kpi-time">
          <div className="kpi-icon">⏱️</div>
          <div className="kpi-content">
            <p className="kpi-label">Thời gian TB (phút)</p>
            <h3 className="kpi-value">{overview?.thoiGianTBPhut || 0}</h3>
          </div>
          <div className="kpi-bar"></div>
        </div>

        <div className="kpi-card kpi-score">
          <div className="kpi-icon">⭐</div>
          <div className="kpi-content">
            <p className="kpi-label">Tổng điểm</p>
            <h3 className="kpi-value">{overview?.tongDiem || 0}</h3>
          </div>
          <div className="kpi-bar"></div>
        </div>
      </div>

      {/* ACTIVITIES - TWO COLUMNS */}
      <div className="activities-grid">
        {/* Most Popular Activities */}
        <div className="analytics-section">
          <div className="section-header">
            <h3>🔥 Hoạt động được tham gia nhiều nhất</h3>
            <span className="section-badge">Top</span>
          </div>
          <div className="bar-list">
            {activities?.top?.map((a, i) => {
              const maxValue = Math.max(...(activities?.top?.map(x => x.soLuot) || [1]));
              const percentage = (a.soLuot / maxValue) * 100;
              return (
                <div key={i} className="bar-row">
                  <div className="bar-rank">{i + 1}</div>
                  <div className="bar-content">
                    <span className="bar-title">{a.tieuDe}</span>
                    <div className="bar-wrapper">
                      <div className="bar">
                        <div
                          className="bar-fill popular"
                          style={{ width: `${percentage}%` }}
                        >
                          <span className="bar-value">{a.soLuot}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Least Popular Activities */}
        <div className="analytics-section">
          <div className="section-header">
            <h3>❄️ Hoạt động ít được tham gia</h3>
            <span className="section-badge">Low</span>
          </div>
          <div className="bar-list">
            {activities?.bottom?.map((a, i) => {
              const maxValue = Math.max(...(activities?.bottom?.map(x => x.soLuot) || [1]));
              const percentage = (a.soLuot / maxValue) * 100;
              return (
                <div key={i} className="bar-row">
                  <div className="bar-rank">{i + 1}</div>
                  <div className="bar-content">
                    <span className="bar-title">{a.tieuDe}</span>
                    <div className="bar-wrapper">
                      <div className="bar">
                        <div
                          className="bar-fill unpopular"
                          style={{ width: `${percentage}%` }}
                        >
                          <span className="bar-value">{a.soLuot}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* STUDENTS */}
      <div className="analytics-section students-section">
        <div className="section-header">
          <h3>👨‍🎓 Học sinh tích cực nhất</h3>
          <span className="section-badge">Top {students?.length || 0}</span>
        </div>
        <div className="table-wrapper">
          <table className="analytics-table">
            <thead>
              <tr>
                <th className="col-rank">Xếp hạng</th>
                <th className="col-name">Tên đăng nhập</th>
                <th className="col-visits">Lượt học</th>
                <th className="col-completion">Hoàn thành</th>
                <th className="col-score">Tổng điểm</th>
              </tr>
            </thead>
            <tbody>
              {students?.map((s, i) => (
                <tr key={i} className={`table-row rank-${i + 1}`}>
                  <td className="col-rank">
                    <span className="rank-badge">
                      {i === 0 ? "🥇" : i === 1 ? "🥈" : i === 2 ? "🥉" : i + 1}
                    </span>
                  </td>
                  <td className="col-name">
                    <span className="student-name">{s.tenDangNhap}</span>
                  </td>
                  <td className="col-visits">
                    <span className="metric-value">{s.soLuot}</span>
                  </td>
                  <td className="col-completion">
                    <span className="metric-badge">{s.soHoanThanh}</span>
                  </td>
                  <td className="col-score">
                    <span className="score-badge">⭐ {s.tongDiem}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}