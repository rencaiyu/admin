import React from 'react';
import { Card, Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import { loginApi } from '../../api/auth/authApi';
import { authStore } from '../../store/authStore';

export default function LoginPage() {
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      const data = await loginApi(values);
      authStore.setToken(data.token);
      authStore.setProfile(data);
      message.success('登录成功');
      navigate('/users');
    } catch (e) {
      message.error(e.message);
    }
  };

  return (
    <div style={{ height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center', background: '#f5f5f5' }}>
      <Card title="后台管理系统登录" style={{ width: 360 }}>
        <Form layout="vertical" onFinish={onFinish} initialValues={{ username: 'admin', password: 'admin' }}>
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="password" label="密码" rules={[{ required: true }]}>
            <Input.Password />
          </Form.Item>
          <Button htmlType="submit" type="primary" block>登录</Button>
        </Form>
      </Card>
    </div>
  );
}
