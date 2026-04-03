import request from '../../utils/request';

export const listRolesApi = (keyword) => request.get('/roles', { params: { keyword } });
export const saveRoleApi = (data) => request.post('/roles', data);
export const deleteRoleApi = (id) => request.delete(`/roles/${id}`);
export const roleMenusApi = (id) => request.get(`/roles/${id}/menus`);
