import React, { useEffect, useMemo, useState } from 'react';
import { Button, Form, Input, Modal, Select, Space, Table, Tag, message } from 'antd';
import { deleteUserApi, listUsersApi, saveUserApi, userRolesApi } from '../../api/user/userApi';
import { listRolesApi } from '../../api/role/roleApi';
import { authStore } from '../../store/authStore';

export default function UserPage() {
  const [list, setList] = useState([]);
  const [roles, setRoles] = useState([]);
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const permissions = authStore.getProfile()?.permissions || [];

  const canAdd = permissions.includes('user:add');
  const canEdit = permissions.includes('user:edit');
  const canDelete = permissions.includes('user:delete');

  const roleMap = useMemo(() => Object.fromEntries(roles.map((r) => [r.id, r.roleName])), [roles]);

  const load = async () => {
    const [users, roleList] = await Promise.all([listUsersApi(), listRolesApi()]);
    setRoles(roleList);
    const merged = await Promise.all(users.map(async (u) => ({ ...u, roleIds: await userRolesApi(u.id) })));
    setList(merged);
  };

  useEffect(() => { load(); }, []);

  const save = async () => {
    setLoading(true);
    try {
      await saveUserApi(form.getFieldsValue());
      message.success('保存成功');
      setOpen(false);
      form.resetFields();
      load();
    } catch (e) { message.error(e.message); }
    setLoading(false);
  };

  const remove = async (id) => {
    await deleteUserApi(id);
    message.success('删除成功');
    load();
  };

  return (
    <>
      <Space style={{ marginBottom: 12 }}>
        <Button type="primary" onClick={() => setOpen(true)} disabled={!canAdd}>新增用户</Button>
      </Space>
      <Table rowKey="id" dataSource={list} columns={[
        { title: '用户名', dataIndex: 'username' },
        { title: '昵称', dataIndex: 'nickname' },
        { title: '状态', dataIndex: 'status', render: (v) => v === 1 ? '启用' : '禁用' },
        {
          title: '角色', dataIndex: 'roleIds', render: (ids) => (
            <Space>{(ids || []).map((id) => <Tag key={id}>{roleMap[id] || id}</Tag>)}</Space>
          )
        },
        {
          title: '操作', render: (_, row) => (
            <Space>
              <Button disabled={!canEdit} onClick={() => { form.setFieldsValue({ ...row, password: '' }); setOpen(true); }}>编辑</Button>
              <Button danger disabled={!canDelete} onClick={() => remove(row.id)}>删除</Button>
            </Space>
          )
        },
      ]} />

      <Modal title="用户" open={open} onOk={save} confirmLoading={loading} onCancel={() => setOpen(false)}>
        <Form form={form} layout="vertical">
          <Form.Item name="id" hidden><Input /></Form.Item>
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="password" label="密码" rules={[{ required: !form.getFieldValue('id') }]}><Input.Password /></Form.Item>
          <Form.Item name="nickname" label="昵称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true }]} initialValue={1}>
            <Select options={[{ label: '启用', value: 1 }, { label: '禁用', value: 0 }]} />
          </Form.Item>
          <Form.Item name="roleIds" label="角色">
            <Select mode="multiple" options={roles.map((r) => ({ label: r.roleName, value: r.id }))} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
