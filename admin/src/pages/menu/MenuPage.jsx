import React, { useEffect, useState } from 'react';
import { Button, Form, Input, InputNumber, Modal, Select, Space, Table, message } from 'antd';
import { deleteMenuApi, listMenusApi, saveMenuApi } from '../../api/menu/menuApi';
import { authStore } from '../../store/authStore';

export default function MenuPage() {
  const [list, setList] = useState([]);
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();
  const permissions = authStore.getProfile()?.permissions || [];
  const canAdd = permissions.includes('menu:add');
  const canEdit = permissions.includes('menu:edit');
  const canDelete = permissions.includes('menu:delete');

  const load = async () => setList(await listMenusApi());
  useEffect(() => { load(); }, []);

  const save = async () => {
    await saveMenuApi(form.getFieldsValue());
    message.success('保存成功');
    setOpen(false);
    form.resetFields();
    load();
  };

  return (
    <>
      <Button type="primary" style={{ marginBottom: 12 }} disabled={!canAdd} onClick={() => setOpen(true)}>新增菜单/按钮</Button>
      <Table rowKey="id" dataSource={list} columns={[
        { title: 'ID', dataIndex: 'id' },
        { title: '父ID', dataIndex: 'parentId' },
        { title: '名称', dataIndex: 'name' },
        { title: '路径', dataIndex: 'path' },
        { title: '权限标识', dataIndex: 'permission' },
        { title: '类型', dataIndex: 'type', render: (v) => v === 1 ? '菜单' : '按钮' },
        {
          title: '操作', render: (_, row) => <Space>
            <Button disabled={!canEdit} onClick={() => { form.setFieldsValue(row); setOpen(true); }}>编辑</Button>
            <Button danger disabled={!canDelete} onClick={async () => { await deleteMenuApi(row.id); message.success('删除成功'); load(); }}>删除</Button>
          </Space>
        }
      ]} />
      <Modal title="菜单/按钮" open={open} onOk={save} onCancel={() => setOpen(false)}>
        <Form layout="vertical" form={form} initialValues={{ type: 1, status: 1, parentId: 0, sort: 1 }}>
          <Form.Item name="id" hidden><Input /></Form.Item>
          <Form.Item name="parentId" label="父级ID"><InputNumber style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="name" label="名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="path" label="路由路径"><Input /></Form.Item>
          <Form.Item name="permission" label="按钮权限标识"><Input /></Form.Item>
          <Form.Item name="type" label="类型" rules={[{ required: true }]}>
            <Select options={[{ label: '菜单', value: 1 }, { label: '按钮', value: 2 }]} />
          </Form.Item>
          <Form.Item name="sort" label="排序" rules={[{ required: true }]}><InputNumber style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true }]}>
            <Select options={[{ label: '启用', value: 1 }, { label: '禁用', value: 0 }]} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
