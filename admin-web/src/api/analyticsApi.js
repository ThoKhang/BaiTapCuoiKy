import axios from "./axios";

export const getAnalyticsOverview = async (fromDate, toDate) => {
  const res = await axios.get("/api/admin/analytics/overview", {
    params: { fromDate, toDate }
  });
  return res.data;
};

export const getAnalyticsActivities = async (fromDate, toDate) => {
  const res = await axios.get("/api/admin/analytics/activities", {
    params: { fromDate, toDate }
  });
  return res.data;
};

export const getAnalyticsStudents = async (fromDate, toDate) => {
  const res = await axios.get("/api/admin/analytics/students", {
    params: { fromDate, toDate }
  });
  return res.data;
};
