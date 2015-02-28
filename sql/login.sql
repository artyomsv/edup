SELECT password
FROM credentials
WHERE status = 'ACTIVE' AND login_id = (SELECT account_id
                                        FROM account_version
                                        WHERE username = 'ADMIN');

SELECT
  role.role,
  'Roles'
FROM user_role
  INNER JOIN role ON user_role.ROLE_ID = role.id
WHERE account_id = (SELECT account_id
                    FROM account_version
                    WHERE username = ?);
