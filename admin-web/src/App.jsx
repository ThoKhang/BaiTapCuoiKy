import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Users from "./pages/Users";
import MonHoc from "./pages/MonHoc";
import HoatDong from "./pages/HoatDong";
import CauHoi from "./pages/CauHoi";
import AdminLayout from "./components/AdminLayout";

function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* LOGIN - layout riêng */}
        <Route path="/login" element={<Login />} />

        {/* DASHBOARD */}
        <Route
          path="/"
          element={
            <PrivateRoute>
              <AdminLayout>
                <Dashboard />
              </AdminLayout>
            </PrivateRoute>
          }
        />

        {/* USERS */}
        <Route
          path="/users"
          element={
            <PrivateRoute>
              <AdminLayout>
                <Users />
              </AdminLayout>
            </PrivateRoute>
          }
        />

        {/* MÔN HỌC */}
        <Route
          path="/mon-hoc"
          element={
            <PrivateRoute>
              <AdminLayout>
                <MonHoc />
              </AdminLayout>
            </PrivateRoute>
          }
        />

        {/* HOẠT ĐỘNG */}
        <Route
          path="/hoat-dong"
          element={
            <PrivateRoute>
              <AdminLayout>
                <HoatDong />
              </AdminLayout>
            </PrivateRoute>
          }
        />

        {/* CÂU HỎI */}
        <Route
          path="/cau-hoi"
          element={
            <PrivateRoute>
              <AdminLayout>
                <CauHoi />
              </AdminLayout>
            </PrivateRoute>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}
