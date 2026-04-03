import React, { useMemo } from 'react';
import { Layout, Menu, Button, Space, Typography } from 'antd';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { authStore } from '../store/authStore';

const { Header, Sider, Content } = Layout;

const flattenMenu = (menus = []) => menus.filter((m) => m.type === 1).map((m) => ({
  key: m.path,
  label: m.name,
  children: (m.children || []).filter((c) => c.type === 1).map((c) => ({ key: c.path, label: c.name })),
}));

export default function MainLayout() {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const profile = authStore.getProfile() || {};

  const menuItems = useMemo(() => flattenMenu(profile.menus || []), [profile.menus]);

  const logout = () => {
    authStore.logout();
    navigate('/login');
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider theme="light">
        <div style={{ color: '#1677ff', fontWeight: 700, textAlign: 'center', margin: 16 }}>Admin</div>
        <Menu
          mode="inline"
          selectedKeys={[pathname]}
          items={menuItems}
          onClick={(e) => navigate(e.key)}
        />
      </Sider>
      <Layout>
        <Header style={{ background: '#fff' }}>
          <Space style={{ float: 'right' }}>
            <Typography.Text>{profile.userInfo?.nickname}</Typography.Text>
            <Button onClick={logout}>退出登录</Button>
          </Space>
        </Header>
        <Content style={{ margin: 16, background: '#fff', padding: 16 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
