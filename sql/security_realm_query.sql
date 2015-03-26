SELECT password
FROM V_ACCOUNT
WHERE username = 'admin';

SELECT
  role,
  'Roles'
FROM V_ROLES
WHERE username = 'admin';


