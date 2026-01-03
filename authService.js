import api from "../api/api";

export const login = async (data) => {
  const res = await api.post("/auth/login", data);
  localStorage.setItem("token", res.data);
};

export const register = async (data) => {
  await api.post("/auth/register", data);
};

export const logout = () => {
  localStorage.clear();
};
