------------------------------------------------------------
-- INSERT ROLES
------------------------------------------------------------
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('MANAGER');
INSERT INTO roles (name) VALUES ('SUPPORT');



------------------------------------------------------------
-- INSERT PERMISSIONS
------------------------------------------------------------
INSERT INTO permissions (name) VALUES ('READ');
INSERT INTO permissions (name) VALUES ('WRITE');
INSERT INTO permissions (name) VALUES ('DELETE');
INSERT INTO permissions (name) VALUES ('UPDATE');
INSERT INTO permissions (name) VALUES ('EXPORT');


------------------------------------------------------------
-- ASSIGN PERMISSIONS TO ADMIN (role_id = 1)
------------------------------------------------------------
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1); -- READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2); -- WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 3); -- DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 4); -- UPDATE
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 5); -- EXPORT

------------------------------------------------------------
-- ASSIGN PERMISSIONS TO USER (role_id = 2)
------------------------------------------------------------
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 1); -- READ

------------------------------------------------------------
-- ASSIGN PERMISSIONS TO MANAGER (role_id = 3)
------------------------------------------------------------
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 1); -- READ
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 2); -- WRITE
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 3); -- DELETE
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 4); -- UPDATE

------------------------------------------------------------
-- ASSIGN PERMISSIONS TO SUPPORT (role_id = 4)
------------------------------------------------------------
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 1); -- READ



------------------------------------------------------------
-- INSERT 51 USERS
------------------------------------------------------------
INSERT INTO user_security (username, email, password) VALUES ('user1', 'user1@example.com', 'pass1');
INSERT INTO user_security (username, email, password) VALUES ('user2', 'user2@example.com', 'pass2');
INSERT INTO user_security (username, email, password) VALUES ('user3', 'user3@example.com', 'pass3');
INSERT INTO user_security (username, email, password) VALUES ('user4', 'user4@example.com', 'pass4');
INSERT INTO user_security (username, email, password) VALUES ('user5', 'user5@example.com', 'pass5');
INSERT INTO user_security (username, email, password) VALUES ('user6', 'user6@example.com', 'pass6');
INSERT INTO user_security (username, email, password) VALUES ('user7', 'user7@example.com', 'pass7');
INSERT INTO user_security (username, email, password) VALUES ('user8', 'user8@example.com', 'pass8');
INSERT INTO user_security (username, email, password) VALUES ('user9', 'user9@example.com', 'pass9');
INSERT INTO user_security (username, email, password) VALUES ('user10', 'user10@example.com', 'pass10');

INSERT INTO user_security (username, email, password) VALUES ('user11', 'user11@example.com', 'pass11');
INSERT INTO user_security (username, email, password) VALUES ('user12', 'user12@example.com', 'pass12');
INSERT INTO user_security (username, email, password) VALUES ('user13', 'user13@example.com', 'pass13');
INSERT INTO user_security (username, email, password) VALUES ('user14', 'user14@example.com', 'pass14');
INSERT INTO user_security (username, email, password) VALUES ('user15', 'user15@example.com', 'pass15');
INSERT INTO user_security (username, email, password) VALUES ('user16', 'user16@example.com', 'pass16');
INSERT INTO user_security (username, email, password) VALUES ('user17', 'user17@example.com', 'pass17');
INSERT INTO user_security (username, email, password) VALUES ('user18', 'user18@example.com', 'pass18');
INSERT INTO user_security (username, email, password) VALUES ('user19', 'user19@example.com', 'pass19');
INSERT INTO user_security (username, email, password) VALUES ('user20', 'user20@example.com', 'pass20');

INSERT INTO user_security (username, email, password) VALUES ('user21', 'user21@example.com', 'pass21');
INSERT INTO user_security (username, email, password) VALUES ('user22', 'user22@example.com', 'pass22');
INSERT INTO user_security (username, email, password) VALUES ('user23', 'user23@example.com', 'pass23');
INSERT INTO user_security (username, email, password) VALUES ('user24', 'user24@example.com', 'pass24');
INSERT INTO user_security (username, email, password) VALUES ('user25', 'user25@example.com', 'pass25');
INSERT INTO user_security (username, email, password) VALUES ('user26', 'user26@example.com', 'pass26');
INSERT INTO user_security (username, email, password) VALUES ('user27', 'user27@example.com', 'pass27');
INSERT INTO user_security (username, email, password) VALUES ('user28', 'user28@example.com', 'pass28');
INSERT INTO user_security (username, email, password) VALUES ('user29', 'user29@example.com', 'pass29');
INSERT INTO user_security (username, email, password) VALUES ('user30', 'user30@example.com', 'pass30');

INSERT INTO user_security (username, email, password) VALUES ('user31', 'user31@example.com', 'pass31');
INSERT INTO user_security (username, email, password) VALUES ('user32', 'user32@example.com', 'pass32');
INSERT INTO user_security (username, email, password) VALUES ('user33', 'user33@example.com', 'pass33');
INSERT INTO user_security (username, email, password) VALUES ('user34', 'user34@example.com', 'pass34');
INSERT INTO user_security (username, email, password) VALUES ('user35', 'user35@example.com', 'pass35');
INSERT INTO user_security (username, email, password) VALUES ('user36', 'user36@example.com', 'pass36');
INSERT INTO user_security (username, email, password) VALUES ('user37', 'user37@example.com', 'pass37');
INSERT INTO user_security (username, email, password) VALUES ('user38', 'user38@example.com', 'pass38');
INSERT INTO user_security (username, email, password) VALUES ('user39', 'user39@example.com', 'pass39');
INSERT INTO user_security (username, email, password) VALUES ('user40', 'user40@example.com', 'pass40');

INSERT INTO user_security (username, email, password) VALUES ('user41', 'user41@example.com', 'pass41');
INSERT INTO user_security (username, email, password) VALUES ('user42', 'user42@example.com', 'pass42');
INSERT INTO user_security (username, email, password) VALUES ('user43', 'user43@example.com', 'pass43');
INSERT INTO user_security (username, email, password) VALUES ('user44', 'user44@example.com', 'pass44');
INSERT INTO user_security (username, email, password) VALUES ('user45', 'user45@example.com', 'pass45');
INSERT INTO user_security (username, email, password) VALUES ('user46', 'user46@example.com', 'pass46');
INSERT INTO user_security (username, email, password) VALUES ('user47', 'user47@example.com', 'pass47');
INSERT INTO user_security (username, email, password) VALUES ('user48', 'user48@example.com', 'pass48');
INSERT INTO user_security (username, email, password) VALUES ('user49', 'user49@example.com', 'pass49');
INSERT INTO user_security (username, email, password) VALUES ('user50', 'user50@example.com', 'pass50');

INSERT INTO user_security (username, email, password) VALUES ('user51', 'user51@example.com', 'pass51');

------------------------------------------------------------
-- INSERT 51 PROFILES
------------------------------------------------------------
INSERT INTO user_profiles (first_name, last_name, phone, avatar_url, user_id)
VALUES 
('User1','Last1','600001',NULL,1),
('User2','Last2','600002',NULL,2),
('User3','Last3','600003',NULL,3),
('User4','Last4','600004',NULL,4),
('User5','Last5','600005',NULL,5),
('User6','Last6','600006',NULL,6),
('User7','Last7','600007',NULL,7),
('User8','Last8','600008',NULL,8),
('User9','Last9','600009',NULL,9),
('User10','Last10','600010',NULL,10),
('User11','Last11','600011',NULL,11),
('User12','Last12','600012',NULL,12),
('User13','Last13','600013',NULL,13),
('User14','Last14','600014',NULL,14),
('User15','Last15','600015',NULL,15),
('User16','Last16','600016',NULL,16),
('User17','Last17','600017',NULL,17),
('User18','Last18','600018',NULL,18),
('User19','Last19','600019',NULL,19),
('User20','Last20','600020',NULL,20),
('User21','Last21','600021',NULL,21),
('User22','Last22','600022',NULL,22),
('User23','Last23','600023',NULL,23),
('User24','Last24','600024',NULL,24),
('User25','Last25','600025',NULL,25),
('User26','Last26','600026',NULL,26),
('User27','Last27','600027',NULL,27),
('User28','Last28','600028',NULL,28),
('User29','Last29','600029',NULL,29),
('User30','Last30','600030',NULL,30),
('User31','Last31','600031',NULL,31),
('User32','Last32','600032',NULL,32),
('User33','Last33','600033',NULL,33),
('User34','Last34','600034',NULL,34),
('User35','Last35','600035',NULL,35),
('User36','Last36','600036',NULL,36),
('User37','Last37','600037',NULL,37),
('User38','Last38','600038',NULL,38),
('User39','Last39','600039',NULL,39),
('User40','Last40','600040',NULL,40),
('User41','Last41','600041',NULL,41),
('User42','Last42','600042',NULL,42),
('User43','Last43','600043',NULL,43),
('User44','Last44','600044',NULL,44),
('User45','Last45','600045',NULL,45),
('User46','Last46','600046',NULL,46),
('User47','Last47','600047',NULL,47),
('User48','Last48','600048',NULL,48),
('User49','Last49','600049',NULL,49),
('User50','Last50','600050',NULL,50),
('User51','Last51','600051',NULL,51);

------------------------------------------------------------
-- ASSIGN MIXED ROLES
------------------------------------------------------------

-- ADMIN users (20 random)
INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (3,3);
INSERT INTO user_roles (user_id, role_id) VALUES (5,3);
INSERT INTO user_roles (user_id, role_id) VALUES (7,4);
INSERT INTO user_roles (user_id, role_id) VALUES (9,1);
INSERT INTO user_roles (user_id, role_id) VALUES (11,1);
INSERT INTO user_roles (user_id, role_id) VALUES (13,1);
INSERT INTO user_roles (user_id, role_id) VALUES (15,1);
INSERT INTO user_roles (user_id, role_id) VALUES (17,1);
INSERT INTO user_roles (user_id, role_id) VALUES (19,1);
INSERT INTO user_roles (user_id, role_id) VALUES (21,1);
INSERT INTO user_roles (user_id, role_id) VALUES (23,1);
INSERT INTO user_roles (user_id, role_id) VALUES (25,1);
INSERT INTO user_roles (user_id, role_id) VALUES (27,1);
INSERT INTO user_roles (user_id, role_id) VALUES (29,1);
INSERT INTO user_roles (user_id, role_id) VALUES (31,1);
INSERT INTO user_roles (user_id, role_id) VALUES (33,1);
INSERT INTO user_roles (user_id, role_id) VALUES (35,1);
INSERT INTO user_roles (user_id, role_id) VALUES (37,1);
INSERT INTO user_roles (user_id, role_id) VALUES (39,1);

-- USER users (rest)
INSERT INTO user_roles (user_id, role_id) VALUES (2,2);
INSERT INTO user_roles (user_id, role_id) VALUES (4,4);
INSERT INTO user_roles (user_id, role_id) VALUES (6,2);
INSERT INTO user_roles (user_id, role_id) VALUES (8,2);
INSERT INTO user_roles (user_id, role_id) VALUES (10,2);
INSERT INTO user_roles (user_id, role_id) VALUES (12,2);
INSERT INTO user_roles (user_id, role_id) VALUES (14,2);
INSERT INTO user_roles (user_id, role_id) VALUES (16,2);
INSERT INTO user_roles (user_id, role_id) VALUES (18,2);
INSERT INTO user_roles (user_id, role_id) VALUES (20,2);
INSERT INTO user_roles (user_id, role_id) VALUES (22,2);
INSERT INTO user_roles (user_id, role_id) VALUES (24,2);
INSERT INTO user_roles (user_id, role_id) VALUES (26,2);
INSERT INTO user_roles (user_id, role_id) VALUES (28,2);
INSERT INTO user_roles (user_id, role_id) VALUES (30,2);
INSERT INTO user_roles (user_id, role_id) VALUES (32,2);
INSERT INTO user_roles (user_id, role_id) VALUES (34,2);
INSERT INTO user_roles (user_id, role_id) VALUES (36,2);
INSERT INTO user_roles (user_id, role_id) VALUES (38,2);
INSERT INTO user_roles (user_id, role_id) VALUES (40,2);
INSERT INTO user_roles (user_id, role_id) VALUES (41,2);
INSERT INTO user_roles (user_id, role_id) VALUES (42,2);
INSERT INTO user_roles (user_id, role_id) VALUES (43,2);
INSERT INTO user_roles (user_id, role_id) VALUES (44,2);
INSERT INTO user_roles (user_id, role_id) VALUES (45,2);
INSERT INTO user_roles (user_id, role_id) VALUES (46,2);
INSERT INTO user_roles (user_id, role_id) VALUES (47,2);
INSERT INTO user_roles (user_id, role_id) VALUES (48,2);
INSERT INTO user_roles (user_id, role_id) VALUES (49,2);
INSERT INTO user_roles (user_id, role_id) VALUES (50,2);
INSERT INTO user_roles (user_id, role_id) VALUES (51,2);