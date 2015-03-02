SELECT credential_version.password FROM credential_version INNER JOIN account ON account.account_id = credential_version.account_fk WHERE account.status = 'APPROVED' AND account.username = 'admin';


SELECT user_role.role, 'Roles' FROM user_role INNER JOIN account ON account.account_id = user_role.account_fk WHERE account.status = 'APPROVED' AND account.username = 'admin';


