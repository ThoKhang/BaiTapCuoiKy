import axios from "./axios";

export const getHoatDong = async () => {
  const res = await axios.get("/api/admin/hoat-dong");
  return res.data;
};

export const getHoatDongById = async (id) => {
  const res = await axios.get(`/api/admin/hoat-dong/${id}`);
  return res.data;
};

export const createHoatDong = async (data) => {
  const res = await axios.post("/api/admin/hoat-dong", data);
  return res.data;
};

export const updateHoatDong = async (id, data) => {
  const res = await axios.put(`/api/admin/hoat-dong/${id}`, data);
  return res.data;
};

export const deleteHoatDong = async (id) => {
  await axios.delete(`/api/admin/hoat-dong/${id}`);
};

export const addCauHoiToHoatDong = async (id, maCauHoi, thuTu) => {
  await axios.post(`/api/admin/hoat-dong/${id}/cau-hoi`, {
    maCauHoi,
    thuTu
  });
};
