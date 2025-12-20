import { Link } from "react-router-dom";

export default function Sidebar() {
  return (
    <div style={{
      width: 220,
      background: "#1e293b",
      color: "white",
      height: "100vh",
      padding: 20
    }}>
      <h3>ADMIN</h3>

      <ul style={{ listStyle: "none", padding: 0 }}>
        <li><Link to="/" style={linkStyle}>Dashboard</Link></li>
        <li><Link to="/users" style={linkStyle}>Người dùng</Link></li>
        <li><Link to="/mon-hoc" style={linkStyle}>Môn học</Link></li>
        <li><Link to="/hoat-dong" style={linkStyle}>Hoạt động</Link></li>
        <li><Link to="/cau-hoi" style={linkStyle}>Câu hỏi</Link></li>
      </ul>
    </div>
  );
}

const linkStyle = {
  color: "white",
  textDecoration: "none",
  display: "block",
  padding: "8px 0"
};
