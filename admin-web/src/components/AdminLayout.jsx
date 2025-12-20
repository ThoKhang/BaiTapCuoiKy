import Sidebar from "./Sidebar";
import Header from "./Header";

export default function AdminLayout({ children }) {
  return (
    <div style={{ display: "flex" }}>
      <Sidebar />
      <div style={{ flex: 1 }}>
        <Header />
        <div style={{ padding: 20 }}>
          {children}
        </div>
      </div>
    </div>
  );
}
