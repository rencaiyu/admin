import request from '../../utils/request';

export const listUsersApi = (keyword) => request.get('/users', { params: { keyword } });
export const saveUserApi = (data) => request.post('/users', data);
export const deleteUserApi = (id) => request.delete(`/users/${id}`);
export const userRolesApi = (id) => request.get(`/users/${id}/roles`);
