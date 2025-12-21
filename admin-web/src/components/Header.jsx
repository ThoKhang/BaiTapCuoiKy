import { useState } from "react";
import "./Header.css";

export default function Header() {
  const [showDropdown, setShowDropdown] = useState(false);

  const logout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  // Lấy tên user từ localStorage hoặc state của bạn
  const userName = localStorage.getItem("userName") || "Quản trị viên";
  const userInitial = userName.charAt(0).toUpperCase();

  return (
    <header className="header">
      <div className="header-content">
        <h2 className="page-title">QUẢN TRỊ HỆ THỐNG ỨNG DỤNG HỌC TẬP CHO TRẺ</h2>
        
        <div className="header-right">
          <div className="notification-icon">
            🔔
            <span className="notification-badge">3</span>
          </div>

          <div className="user-profile" onClick={() => setShowDropdown(!showDropdown)}>
            <div className="user-avatar">{userInitial}</div>
            <div className="user-info">
              <p className="user-name">{userName}</p>
              <p className="user-role">Super Admin</p>
            </div>
            <span className="dropdown-arrow">▼</span>

            {showDropdown && (
              <div className="dropdown-menu">
                <a href="#profile" className="dropdown-item">👤 Hồ sơ cá nhân</a>
                <a href="#settings" className="dropdown-item">⚙️ Cài đặt</a>
                <hr className="dropdown-divider" />
                <button onClick={logout} className="dropdown-item logout-item">
                  🚪 Đăng xuất
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
}