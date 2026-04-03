import axios from 'axios';

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 8000,
});

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

request.interceptors.response.use((response) => {
  const resp = response.data;
  if (resp.code !== 0) {
    return Promise.reject(new Error(resp.message || '请求失败'));
  }
  return resp.data;
});

export default request;
