import React, { useEffect, useState } from 'react';
import { Button, Form, Input, Modal, Select, Space, Table, TreeSelect, message } from 'antd';
import { deleteRoleApi, listRolesApi, roleMenusApi, saveRoleApi } from '../../api/role/roleApi';
import { listMenusApi } from '../../api/menu/menuApi';
import { authStore } from '../../store/authStore';

export default function RolePage() {
  const [list, setList] = useState([]);
  const [menuTree, setMenuTree] = useState([]);
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();
  const permissions = authStore.getProfile()?.permissions || [];

  const canAdd = permissions.includes('role:add');
  const canEdit = permissions.includes('role:edit');
  const canDelete = permissions.includes('role:delete');

  const buildTree = (flat) => {
    const map = {};
    flat.forEach((i) => map[i.id] = { title: `${i.name}${i.type === 2 ? ' (按钮)' : ''}`, value: i.id, key: i.id, parentId: i.parentId, children: [] });
    const roots = [];
    Object.values(map).forEach((item) => {
      if (item.parentId === 0) roots.push(item);
      else if (map[item.parentId]) map[item.parentId].children.push(item);
      else roots.push(item);
    });
    return roots;
  };

  const load = async () => {
    const [roles, menus] = await Promise.all([listRolesApi(), listMenusApi()]);
    setList(roles);
    setMenuTree(buildTree(menus));
  };
  useEffect(() => { load(); }, []);

  const save = async () => {
    await saveRoleApi(form.getFieldsValue());
    message.success('保存成功');
    setOpen(false);
    form.resetFields();
    load();
  };

  const edit = async (row) => {
    const menuIds = await roleMenusApi(row.id);
    form.setFieldsValue({ ...row, menuIds });
    setOpen(true);
  };

  return (
    <>
      <Button type="primary" style={{ marginBottom: 12 }} disabled={!canAdd} onClick={() => setOpen(true)}>新增角色</Button>
      <Table rowKey="id" dataSource={list} columns={[
        { title: '角色编码', dataIndex: 'roleCode' },
        { title: '角色名称', dataIndex: 'roleName' },
        { title: '状态', dataIndex: 'status', render: (v) => v === 1 ? '启用' : '禁用' },
        {
          title: '操作', render: (_, row) => <Space>
            <Button disabled={!canEdit} onClick={() => edit(row)}>编辑</Button>
            <Button danger disabled={!canDelete} onClick={async () => { await deleteRoleApi(row.id); message.success('删除成功'); load(); }}>删除</Button>
          </Space>
        }
      ]} />
      <Modal title="角色" open={open} onOk={save} onCancel={() => setOpen(false)}>
        <Form layout="vertical" form={form} initialValues={{ status: 1 }}>
          <Form.Item name="id" hidden><Input /></Form.Item>
          <Form.Item name="roleCode" label="角色编码" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="roleName" label="角色名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true }]}>
            <Select options={[{ label: '启用', value: 1 }, { label: '禁用', value: 0 }]} />
          </Form.Item>
          <Form.Item name="menuIds" label="菜单与按钮权限">
            <TreeSelect treeData={menuTree} treeCheckable showCheckedStrategy={TreeSelect.SHOW_PARENT} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
