INSERT INTO lms.permissions (id, name, description, date_created)
VALUES (1, 'READ_ROLES', 'can read roles', CURRENT_TIMESTAMP),
       (2, 'WRITE_ROLES', 'can create or update roles', CURRENT_TIMESTAMP),
       (3, 'DELETE_ROLES', 'can delete roles', CURRENT_TIMESTAMP),
       (4, 'READ_USERS', 'can read users', CURRENT_TIMESTAMP),
       (5, 'WRITE_USERS', 'can create or update users', CURRENT_TIMESTAMP),
       (6, 'DELETE_USERS', 'can delete users', CURRENT_TIMESTAMP),
       (7, 'LOGIN', 'can login', CURRENT_TIMESTAMP),
       (8, 'READ_ACCOUNTS', 'can read accounts', CURRENT_TIMESTAMP),
       (9, 'WRITE_ACCOUNTS', 'can create or update accounts', CURRENT_TIMESTAMP),
       (10, 'DELETE_ACCOUNTS', 'can delete accounts', CURRENT_TIMESTAMP),
       (11, 'READ_ACCOUNT_BALANCES', 'can read account balances', CURRENT_TIMESTAMP),
       (12, 'WRITE_ACCOUNT_BALANCES', 'can create or update account balances', CURRENT_TIMESTAMP),
       (13, 'DELETE_ACCOUNT_BALANCES', 'can delete account balances', CURRENT_TIMESTAMP),
       (14, 'READ_ACCOUNT_STATUSES', 'can read account statuses', CURRENT_TIMESTAMP),
       (15, 'WRITE_ACCOUNT_STATUSES', 'can create or update account statuses', CURRENT_TIMESTAMP),
       (16, 'DELETE_ACCOUNT_STATUSES', 'can delete account statuses', CURRENT_TIMESTAMP),
       (17, 'READ_ACCOUNT_TYPES', 'can read account types', CURRENT_TIMESTAMP),
       (18, 'WRITE_ACCOUNT_TYPES', 'can create or update account types', CURRENT_TIMESTAMP),
       (19, 'DELETE_ACCOUNT_TYPES', 'can delete account types', CURRENT_TIMESTAMP),
       (20, 'READ_CURRENCIES', 'can read currencies', CURRENT_TIMESTAMP),
       (21, 'WRITE_CURRENCIES', 'can create or update currencies', CURRENT_TIMESTAMP),
       (22, 'DELETE_CURRENCIES', 'can delete currencies', CURRENT_TIMESTAMP),
       (23, 'READ_PARTNER_ACCOUNTS', 'can read partner accounts', CURRENT_TIMESTAMP),
       (24, 'WRITE_PARTNER_ACCOUNTS', 'can create or update partner accounts', CURRENT_TIMESTAMP),
       (25, 'DELETE_PARTNER_ACCOUNTS', 'can delete partner accounts', CURRENT_TIMESTAMP),
       (26, 'READ_CREDENTIALS', 'can read credentials', CURRENT_TIMESTAMP),
       (27, 'WRITE_CREDENTIALS', 'can create or update credentials', CURRENT_TIMESTAMP),
       (28, 'DELETE_CREDENTIALS', 'can delete credentials', CURRENT_TIMESTAMP),
       (29, 'READ_LOGIN_ATTEMPTS', 'can read login attempts', CURRENT_TIMESTAMP),
       (30, 'WRITE_LOGIN_ATTEMPTS', 'can create or update login attempts', CURRENT_TIMESTAMP),
       (31, 'DELETE_LOGIN_ATTEMPTS', 'can delete login attempts', CURRENT_TIMESTAMP),
       (32, 'READ_TRANSACTION_TYPES', 'can read transaction types', CURRENT_TIMESTAMP),
       (33, 'WRITE_TRANSACTION_TYPES', 'can create or update transaction types', CURRENT_TIMESTAMP),
       (34, 'DELETE_TRANSACTION_TYPES', 'can delete transaction types', CURRENT_TIMESTAMP),
       (35, 'READ_TRANSACTION_TYPE_COMPONENTS', 'can read transaction type components', CURRENT_TIMESTAMP),
       (36, 'WRITE_TRANSACTION_TYPE_COMPONENTS', 'can create or update transaction type components', CURRENT_TIMESTAMP),
       (37, 'DELETE_TRANSACTION_TYPE_COMPONENTS', 'can delete transaction type components', CURRENT_TIMESTAMP),
       (38, 'READ_TRANSACTIONS', 'can read transactions', CURRENT_TIMESTAMP),
       (39, 'WRITE_TRANSACTIONS', 'can create or update transactions', CURRENT_TIMESTAMP),
       (40, 'DELETE_TRANSACTIONS', 'can delete transactions', CURRENT_TIMESTAMP),
       (41, 'READ_TRANSACTION_ENTRIES', 'can read transaction entries', CURRENT_TIMESTAMP),
       (42, 'WRITE_TRANSACTION_ENTRIES', 'can create or update transaction entries', CURRENT_TIMESTAMP),
       (43, 'DELETE_TRANSACTION_ENTRIES', 'can delete transaction entries', CURRENT_TIMESTAMP),
       (44, 'READ_REASON_TYPES', 'can read reason types', CURRENT_TIMESTAMP),
       (45, 'WRITE_REASON_TYPES', 'can create or update reason types', CURRENT_TIMESTAMP),
       (46, 'DELETE_REASON_TYPES', 'can delete reason types', CURRENT_TIMESTAMP),
       (47, 'READ_LOAN_PRODUCTS', 'can read loan products', CURRENT_TIMESTAMP),
       (48, 'WRITE_LOAN_PRODUCTS', 'can create or update loan products', CURRENT_TIMESTAMP),
       (49, 'DELETE_LOAN_PRODUCTS', 'can delete loan products', CURRENT_TIMESTAMP),
       (50, 'READ_REPORTS', 'can read reports', CURRENT_TIMESTAMP),
       (51, 'WRITE_REPORTS', 'can create or update reports', CURRENT_TIMESTAMP),
       (52, 'DELETE_REPORTS', 'can delete reports', CURRENT_TIMESTAMP);;

INSERT INTO lms.roles (id, name, description, date_created)
VALUES (1, 'SYSADMIN', 'system administrator', CURRENT_TIMESTAMP);;

INSERT INTO lms.role_permissions (id, role_id, permission_id, date_created)
VALUES (1, 1, 1, CURRENT_TIMESTAMP),
       (2, 1, 2, CURRENT_TIMESTAMP),
       (3, 1, 3, CURRENT_TIMESTAMP),
       (4, 1, 4, CURRENT_TIMESTAMP),
       (5, 1, 5, CURRENT_TIMESTAMP),
       (6, 1, 6, CURRENT_TIMESTAMP),
       (7, 1, 7, CURRENT_TIMESTAMP),
       (8, 1, 8, CURRENT_TIMESTAMP),
       (9, 1, 9, CURRENT_TIMESTAMP),
       (10, 1, 10, CURRENT_TIMESTAMP),
       (11, 1, 11, CURRENT_TIMESTAMP),
       (12, 1, 12, CURRENT_TIMESTAMP),
       (13, 1, 13, CURRENT_TIMESTAMP),
       (14, 1, 14, CURRENT_TIMESTAMP),
       (15, 1, 15, CURRENT_TIMESTAMP),
       (16, 1, 16, CURRENT_TIMESTAMP),
       (17, 1, 17, CURRENT_TIMESTAMP),
       (18, 1, 18, CURRENT_TIMESTAMP),
       (19, 1, 19, CURRENT_TIMESTAMP),
       (20, 1, 20, CURRENT_TIMESTAMP),
       (21, 1, 21, CURRENT_TIMESTAMP),
       (22, 1, 22, CURRENT_TIMESTAMP),
       (23, 1, 23, CURRENT_TIMESTAMP),
       (24, 1, 24, CURRENT_TIMESTAMP),
       (25, 1, 25, CURRENT_TIMESTAMP),
       (26, 1, 26, CURRENT_TIMESTAMP),
       (27, 1, 27, CURRENT_TIMESTAMP),
       (28, 1, 28, CURRENT_TIMESTAMP),
       (29, 1, 29, CURRENT_TIMESTAMP),
       (30, 1, 30, CURRENT_TIMESTAMP),
       (31, 1, 31, CURRENT_TIMESTAMP),
       (32, 1, 32, CURRENT_TIMESTAMP),
       (33, 1, 33, CURRENT_TIMESTAMP),
       (34, 1, 34, CURRENT_TIMESTAMP),
       (35, 1, 35, CURRENT_TIMESTAMP),
       (36, 1, 36, CURRENT_TIMESTAMP),
       (37, 1, 37, CURRENT_TIMESTAMP),
       (38, 1, 38, CURRENT_TIMESTAMP),
       (39, 1, 39, CURRENT_TIMESTAMP),
       (40, 1, 40, CURRENT_TIMESTAMP),
       (41, 1, 41, CURRENT_TIMESTAMP),
       (42, 1, 42, CURRENT_TIMESTAMP),
       (43, 1, 43, CURRENT_TIMESTAMP),
       (44, 1, 44, CURRENT_TIMESTAMP),
       (45, 1, 45, CURRENT_TIMESTAMP),
       (46, 1, 46, CURRENT_TIMESTAMP),
       (47, 1, 47, CURRENT_TIMESTAMP),
       (48, 1, 48, CURRENT_TIMESTAMP),
       (49, 1, 49, CURRENT_TIMESTAMP),
       (50, 1, 50, CURRENT_TIMESTAMP),
       (51, 1, 51, CURRENT_TIMESTAMP),
       (52, 1, 52, CURRENT_TIMESTAMP);;

INSERT INTO lms.credential_statuses (id, name, description, date_created)
VALUES (1, 'ACTIVE', 'Active', CURRENT_TIMESTAMP),
       (2, 'INACTIVE', 'Inactive', CURRENT_TIMESTAMP),
       (3, 'FROZEN', 'Frozen', CURRENT_TIMESTAMP),
       (4, 'BLOCKED', 'Blocked', CURRENT_TIMESTAMP);;

INSERT INTO lms.account_statuses (id, name, description, date_created)
VALUES (1, 'ACTIVE', 'Active', CURRENT_TIMESTAMP),
       (2, 'PENDING_APPROVAL', 'Pending Approval', CURRENT_TIMESTAMP),
       (3, 'SUSPENDED', 'Suspended', CURRENT_TIMESTAMP),
       (4, 'CLOSED', 'Closed', CURRENT_TIMESTAMP);;

INSERT INTO lms.account_types (id, name, description, parent_account_type_id, date_created)
VALUES (1, 'ASSETS', 'Root asset account', NULL, CURRENT_TIMESTAMP),
       (2, 'LIABILITIES', 'Root liabilities account', NULL, CURRENT_TIMESTAMP),
       (3, 'EQUITY', 'Root equity account', NULL, CURRENT_TIMESTAMP),
       (4, 'INCOME', 'Root income account', NULL, CURRENT_TIMESTAMP),
       (5, 'EXPENSES', 'Root expenses account', NULL, CURRENT_TIMESTAMP);;

INSERT INTO lms.entities(id, name, entity_type, date_updated)
VALUES (1, 'SYSADMIN', 'SYSTEM', CURRENT_TIMESTAMP);;

INSERT INTO lms.entity_contacts(id, contact, entity_id, is_primary, contact_type, date_created)
VALUES (1, 'system-admin@test-lms.xyz', 1, TRUE, 'EMAIL', CURRENT_TIMESTAMP);;

-- plaintext: 123456
INSERT INTO lms.entity_credentials(id, entity_id, hashed_password, credentials_status_id, date_created)
VALUES (1, 1, '$2a$12$0dnWq4Koscn6WiyLdTc8OeajjbZq9LaTmGaVexsA5nA.FIBUBmvTq', 1, CURRENT_TIMESTAMP);;

INSERT INTO lms.user_roles(id, entity_id, role_id, date_created)
VALUES (1, 1, 1, CURRENT_TIMESTAMP);;