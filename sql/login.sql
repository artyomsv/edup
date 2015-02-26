select password from credentials where status = 'ACTIVE' and login_id = (select id from login where username = 'ADMIN');
