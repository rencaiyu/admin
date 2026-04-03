import request from '../../utils/request';

export const listMenusApi = (keyword) => request.get('/menus', { params: { keyword } });
export const saveMenuApi = (data) => request.post('/menus', data);
export const deleteMenuApi = (id) => request.delete(`/menus/${id}`);
