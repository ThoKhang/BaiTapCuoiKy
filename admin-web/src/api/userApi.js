import axios from "./axios";

export const getUsers = async () => {
  const res = await axios.get("/api/admin/users");
  return res.data;
};

export const createUser = async (data) => {
  const res = await axios.post("/api/admin/users", data);
  return res.data;
};

export const updateUser = async (id, data) => {
  const res = await axios.put(`/api/admin/users/${id}`, data);
  return res.data;
};

export const deleteUser = async (id) => {
  await axios.delete(`/api/admin/users/${id}`);
};

export const resetPassword = async (id, newPassword) => {
  await axios.put(
    `/api/admin/users/${id}/reset-password`,
    newPassword,
    { headers: { "Content-Type": "text/plain" } }
  );
};
