-- Insert Users
INSERT INTO app_user (id, name, password) VALUES
                                              (1, 'user', '$2a$10$NKpjx9YIpgH0nkg4VnkQAeUtc9urzhEGajzeTfGzAVzE/8hzgsYQe'),
                                              (2, 'admin', '$2a$10$NKpjx9YIpgH0nkg4VnkQAeUtc9urzhEGajzeTfGzAVzE/8hzgsYQe');

-- Assign Roles
INSERT INTO app_user_roles (user_id, roles) VALUES
                                                (1, 'USER'),
                                                (2, 'ADMIN');
