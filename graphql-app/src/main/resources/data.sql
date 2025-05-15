INSERT INTO users_groups (id,name,email)
VALUES
(1,'Grupo México','mexico@demo.com'),
(2,'Grupo Puebla','puebla@demo.com');

INSERT INTO users (name, email, active, created_at, updated_at,group_id) 
VALUES 
('John Doe', 'john.doe@example.com', 1,'2020-03-01 18:04:00', '2022-01-01 00:00:00',1),
('William Estrada', 'william.estrada@example.com', 0,'2021-01-01 09:03:00', '2023-01-01 00:00:00',1),
('John Dominguez', 'john.do@example.com', 1,'2022-05-30 18:04:00', '2023-04-06 00:00:00',2),
('William Jimenez', 'william.jimenez@example.com', 1,'2023-02-01 09:03:00', '2024-04-01 00:00:00',2);