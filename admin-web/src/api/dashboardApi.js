import axios from "./axios";

export const getDashboard = async () => {
  const res = await axios.get("/api/admin/dashboard");
  return res.data;
};
export const getDashboardCharts = async (fromDate, toDate) => {
  const res = await axios.get("/api/admin/dashboard/charts", {
    params: { fromDate, toDate }
  });
  return res.data;
};
