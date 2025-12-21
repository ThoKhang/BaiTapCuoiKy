import axios from "./axios";

// ===== CÂU HỎI =====
export const getCauHoi = async () => {
  const res = await axios.get("/api/admin/cau-hoi");
  return res.data;
};

export const createCauHoi = async (data) => {
  const res = await axios.post("/api/admin/cau-hoi", data);
  return res.data;
};

export const deleteCauHoi = async (id) => {
  await axios.delete(`/api/admin/cau-hoi/${id}`);
};

// ===== ĐÁP ÁN =====
export const getDapAn = async (maCauHoi) => {
  const res = await axios.get(`/api/admin/cau-hoi/${maCauHoi}/dap-an`);
  return res.data;
};

export const createDapAn = async (maCauHoi, data) => {
  const res = await axios.post(
    `/api/admin/cau-hoi/${maCauHoi}/dap-an`,
    data
  );
  return res.data;
};

export const deleteDapAn = async (maDapAn) => {
  await axios.delete(`/api/admin/dap-an/${maDapAn}`);
};
