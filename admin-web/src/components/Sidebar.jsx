import { Link, useLocation } from "react-router-dom";
import "./Sidebar.css";

export default function Sidebar() {
  const location = useLocation();

  const isActive = (path) => location.pathname === path;
  const logout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };
  
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <div className="logo-icon">📚</div>
        <div className="logo-text">
          <h1>5AE ADMIN</h1>
          <p>Management System</p>
        </div>
      </div>

      <div className="sidebar-sections">
        <div className="section">
          <h3 className="section-title">TỔNG QUAN</h3>
          <nav className="sidebar-nav">
            <Link 
              to="/" 
              className={`nav-item ${isActive('/') ? 'active' : ''}`}
            >
              <span className="nav-icon">📊</span>
              <span className="nav-text">Dashboard</span>
            </Link>
          </nav>
        </div>

        <div className="section">
          <h3 className="section-title">QUẢN LÝ HỌC TẬP</h3>
          <nav className="sidebar-nav">
            <Link 
              to="/mon-hoc" 
              className={`nav-item ${isActive('/mon-hoc') ? 'active' : ''}`}
            >
              <span className="nav-icon">📖</span>
              <span className="nav-text">Môn học</span>
            </Link>
            <Link 
              to="/cau-hoi" 
              className={`nav-item ${isActive('/cau-hoi') ? 'active' : ''}`}
            >
              <span className="nav-icon">❓</span>
              <span className="nav-text">Câu hỏi</span>
            </Link>
            <Link 
              to="/hoat-dong" 
              className={`nav-item ${isActive('/hoat-dong') ? 'active' : ''}`}
            >
              <span className="nav-icon">⚡</span>
              <span className="nav-text">Hoạt động</span>
            </Link>
          </nav>
        </div>

        <div className="section">
          <h3 className="section-title">NGƯỜI DÙNG</h3>
          <nav className="sidebar-nav">
            <Link 
              to="/users" 
              className={`nav-item ${isActive('/users') ? 'active' : ''}`}
            >
              <span className="nav-icon">👥</span>
              <span className="nav-text">Học sinh</span>
            </Link>
          </nav>
        </div>
      </div>

      <div className="sidebar-footer">
        <button onClick={logout} className="logout-btn">
          🚪 Đăng xuất
        </button>
      </div>
    </aside>
  );
}