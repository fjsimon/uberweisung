
-- USERS
-- non-encrypted pw: letmein
INSERT INTO security_user (id, username, password, first_name, last_name) VALUES
(1,  'admin', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'Administrator', 'Adminstrator'),
(2,  'user_test', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'User', 'User');

-- ROLES
INSERT INTO user_role(user_id, role_id) VALUES
 (1, 1), -- give admin ROLE_ADMIN
 (2, 2);  -- give Joe ROLE_USER

