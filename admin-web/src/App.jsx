import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Users from "./pages/Users";
import MonHoc from "./pages/MonHoc";
import HoatDong from "./pages/HoatDong";
import CauHoi from "./pages/CauHoi";
import AdminLayout from "./components/AdminLayout";

function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Login />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/login" element={<Login />} />

        <Route path="/" element={
          <PrivateRoute>
            <AdminLayout>
              <Dashboard />
            </AdminLayout>
          </PrivateRoute>
        } />

        <Route path="/users" element={
          <PrivateRoute>
            <AdminLayout>
              <Users />
            </AdminLayout>
          </PrivateRoute>
        } />

        <Route path="/mon-hoc" element={
          <PrivateRoute>
            <AdminLayout>
              <MonHoc />
            </AdminLayout>
          </PrivateRoute>
        } />

        <Route path="/hoat-dong" element={
          <PrivateRoute>
            <AdminLayout>
              <HoatDong />
            </AdminLayout>
          </PrivateRoute>
        } />

        <Route path="/cau-hoi" element={
          <PrivateRoute>
            <AdminLayout>
              <CauHoi />
            </AdminLayout>
          </PrivateRoute>
        } />

      </Routes>
    </BrowserRouter>
  );
}
