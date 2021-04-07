insert into users(id, username, password, active)
values (1, 'admin', '$2a$10$yX7Eam8nv5axtddlVpJeEOj7ozBdI89d1rdDE92VdynCGJCyv4Dbi', true);
insert into user_role(user_id, roles) VALUES (1, 'ADMIN'), (1, 'USER');