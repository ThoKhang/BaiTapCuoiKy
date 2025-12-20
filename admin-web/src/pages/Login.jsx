import { useState } from "react";
import { login } from "../api/authApi";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    try {
      const res = await login(username, password);
      localStorage.setItem("token", res.token);
      window.location.href = "/";
    } catch{
      alert("Sai tài khoản hoặc mật khẩu");
    }
  };

  return (
    <div style={{ padding: 50 }}>
      <h2>Admin Login</h2>
      <input placeholder="Username" onChange={e => setUsername(e.target.value)} />
      <br /><br />
      <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} />
      <br /><br />
      <button onClick={handleLogin}>Login</button>
    </div>
  );
}
