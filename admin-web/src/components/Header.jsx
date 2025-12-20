export default function Header() {
  const logout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  return (
    <div style={{
      padding: 10,
      background: "#f1f5f9",
      display: "flex",
      justifyContent: "flex-end"
    }}>
      <button onClick={logout}>Đăng xuất</button>
    </div>
  );
}
