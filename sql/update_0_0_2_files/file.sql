CREATE OR REPLACE FUNCTION bytea_import(p_path TEXT, p_result OUT BYTEA)
LANGUAGE plpgsql AS $$
DECLARE
  l_oid OID;
  r     RECORD;
BEGIN
  p_result := '';
  SELECT lo_import(p_path)
  INTO l_oid;
  FOR r IN (SELECT data
            FROM pg_largeobject
            WHERE loid = l_oid
            ORDER BY pageno) LOOP
    p_result = p_result || r.data;
  END LOOP;
  PERFORM lo_unlink(l_oid);
END;$$;

INSERT INTO files (file_id, name, content_type, size, uploaded_by, created, data, checksum)
VALUES (nextval('FILES_SEQUENCE'), 'testname.jpg', 'jpg', 10649, NULL, CURRENT_TIMESTAMP, bytea_import('/Users/Artyom/Desktop/Java/Projects/edup/sql/update_0_0_2_files/unknown.png') , 3505603560);
