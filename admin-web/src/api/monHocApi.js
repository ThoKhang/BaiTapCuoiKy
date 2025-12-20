import axios from "./axios";

// Lấy danh sách môn học
export const getMonHoc = async () => {
  const res = await axios.get("/api/admin/mon-hoc");
  return res.data;
};

// Thêm môn học
export const createMonHoc = async (data) => {
  const res = await axios.post("/api/admin/mon-hoc", data);
  return res.data;
};

// Xóa môn học
export const deleteMonHoc = async (maMonHoc) => {
  await axios.delete(`/api/admin/mon-hoc/${maMonHoc}`);
};
