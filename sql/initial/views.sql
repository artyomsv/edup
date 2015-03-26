DROP VIEW public.V_ACCOUNT RESTRICT;
CREATE OR REPLACE VIEW V_ACCOUNT AS
  SELECT
    a.username,
    c.password,
    a.status
  FROM account a, credential_version c
  WHERE
    a.credential_version_fk = c.id
    AND
    a.status = 'APPROVED';


DROP VIEW public.V_ROLES RESTRICT;
CREATE VIEW V_ROLES AS
  SELECT
    a.username,
    u.role,
    a.status
  FROM account a, user_role u
  WHERE
    a.id = u.account_fk
    AND
    a.status = 'APPROVED';
