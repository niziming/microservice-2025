-- MyBatis-Plus版本的初始化数据SQL脚本

-- 创建示例客户
INSERT INTO customers (id, name, email, customer_type, active, created_at, last_modified_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', '张三', 'zhangsan@example.com', 'REGULAR', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440002', '李四', 'lisi@example.com', 'VIP', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440003', '王五科技有限公司', 'wangwu@company.com', 'ENTERPRISE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 创建示例商品
INSERT INTO products (id, name, description, price, currency, stock_quantity, available, created_at, last_modified_at) VALUES
('550e8400-e29b-41d4-a716-446655440101', 'iPhone 15 Pro', '苹果最新款智能手机', 8999.00, 'CNY', 50, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440102', 'MacBook Pro', '苹果笔记本电脑', 15999.00, 'CNY', 20, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440103', 'AirPods Pro', '苹果无线耳机', 1999.00, 'CNY', 100, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440104', 'iPad Air', '苹果平板电脑', 4999.00, 'CNY', 30, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);