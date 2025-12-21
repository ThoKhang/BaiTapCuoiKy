import { useState } from "react";
import { login } from "../api/authApi";
import "./Login.css";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    
    if (!username.trim() || !password.trim()) {
      setError("Vui lòng nhập tài khoản và mật khẩu");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const res = await login(username, password);
      localStorage.setItem("token", res.token);
      window.location.href = "/";
    } catch {
      setError("❌ Sai tài khoản hoặc mật khẩu");
      setPassword("");
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleLogin(e);
    }
  };

  return (
    <div className="login-container">
      <div className="login-wrapper">
        {/* Left Side - Branding */}
        <div className="login-branding">
          <div className="brand-content">
            <div className="brand-icon">📚</div>
            <h1>5AE Admin</h1>
            <p>Hệ thống quản trị app học tập</p>
            
            <div className="brand-features">
              <div className="feature">
                <span className="feature-icon">✨</span>
                <span>Quản lý App</span>
              </div>
              <div className="feature">
                <span className="feature-icon">📊</span>
                <span>Thống kê chi tiết</span>
              </div>
              <div className="feature">
                <span className="feature-icon">🎓</span>
                <span>Quản lý người dùng và chức năng</span>
              </div>
            </div>
          </div>
        </div>

        {/* Right Side - Login Form */}
        <div className="login-form-wrapper">
          <div className="login-form">
            <h2>Đăng nhập</h2>
            <p className="login-subtitle">Quản trị hệ thống học tập</p>

            {error && <div className="alert-error">{error}</div>}

            <form onSubmit={handleLogin}>
              <div className="form-group">
                <label htmlFor="username">Tài khoản</label>
                <div className="input-wrapper">
                  <input
                    id="username"
                    type="text"
                    placeholder="Nhập tài khoản của bạn"
                    value={username}
                    onChange={e => {
                      setUsername(e.target.value);
                      setError("");
                    }}
                    onKeyPress={handleKeyPress}
                    className="form-input"
                  />
                  <span className="input-icon">👤</span>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="password">Mật khẩu</label>
                <div className="input-wrapper">
                  <input
                    id="password"
                    type={showPassword ? "text" : "password"}
                    placeholder="Nhập mật khẩu của bạn"
                    value={password}
                    onChange={e => {
                      setPassword(e.target.value);
                      setError("");
                    }}
                    onKeyPress={handleKeyPress}
                    className="form-input"
                  />
                  <button
                    type="button"
                    className="show-password-btn"
                    onClick={() => setShowPassword(!showPassword)}
                    title={showPassword ? "Ẩn mật khẩu" : "Hiển thị mật khẩu"}
                  >
                    {showPassword ? "👁️" : "👁️‍🗨️"}
                  </button>
                </div>
              </div>

              <button
                type="submit"
                className="login-btn"
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span className="spinner"></span>
                    Đang đăng nhập...
                  </>
                ) : (
                  <>
                    🔑 Đăng nhập
                  </>
                )}
              </button>
            </form>

            <div className="login-footer">
              <p className="footer-text">
                Liên hệ admin nếu quên mật khẩu
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}