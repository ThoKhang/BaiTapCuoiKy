import axios from "./axios";

export const login = async (tenDangNhap, matKhau) => {
  const res = await axios.post("/api/auth/login", {
    tenDangNhap,
    matKhau,
  });
  return res.data;
};
