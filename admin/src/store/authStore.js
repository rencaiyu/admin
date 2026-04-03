export const authStore = {
  getToken: () => localStorage.getItem('token'),
  setToken: (token) => localStorage.setItem('token', token || ''),
  clearToken: () => localStorage.removeItem('token'),

  getProfile: () => {
    const str = localStorage.getItem('profile');
    return str ? JSON.parse(str) : null;
  },
  setProfile: (profile) => localStorage.setItem('profile', JSON.stringify(profile || {})),
  clearProfile: () => localStorage.removeItem('profile'),

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('profile');
  },
};
