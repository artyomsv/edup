-- CREATE DATABASE edup OWNER postgres;

DROP VIEW public.V_STUDENT_DOCUMENTS;
DROP TABLE public.STUDENT_DOCUMENTS;

DROP TABLE public.files;
DROP SEQUENCE public.file_sequence RESTRICT;

DROP VIEW public.V_STUDENT_BALANCE RESTRICT;
DROP TABLE public.STUDENT_TRANSACTIONS;
DROP TABLE public.TRANSACTION_TYPE;
DROP SEQUENCE public.STUDENT_TRANSACTIONS_SEQUENCE RESTRICT;
DROP SEQUENCE public.TRANSACTIONS_TYPE_SEQUENCE RESTRICT;

DROP VIEW public.v_students RESTRICT;
DROP TABLE public.student_properties;
DROP TABLE public.students_version_mapping;
DROP TABLE public.students;

DROP SEQUENCE public.student_id_sequence RESTRICT;
DROP SEQUENCE public.student_property_sequence RESTRICT;
DROP SEQUENCE public.student_version_sequence RESTRICT;
DROP FUNCTION public.getstudentid();
DROP FUNCTION public.update_current_student_version();


DROP SEQUENCE public.STUDENT_DOCUMENTS_SEQUENCE RESTRICT;
DROP SEQUENCE public.FAKTURA_REPORT_ID_SEQUENCE RESTRICT;

------------------------------------------------------------------------------------------------------
-------------------------------------- 0.0.2 ---------------------------------------------------------
------------------------------------------------------------------------------------------------------
-- Create sequence for files.
CREATE SEQUENCE FILES_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
-- Create file table.
CREATE TABLE FILES
(
  FILE_ID      BIGINT       NOT NULL,
  NAME         VARCHAR(256) NOT NULL,
  CONTENT_TYPE VARCHAR(256) NOT NULL,
  SIZE         BIGINT       NOT NULL,
  UPLOADED_BY  VARCHAR(64),
  CREATED      TIMESTAMP WITH TIME ZONE,
  DATA         BYTEA,
  CHECKSUM     BIGINT       NOT NULL,
  CONSTRAINT FILE_PKEY PRIMARY KEY (FILE_ID)
);
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------




------------------------------------------------------------------------------------------------------
-------------------------------------- 0.0.3 ---------------------------------------------------------
------------------------------------------------------------------------------------------------------
-- Create sequence for students.
CREATE SEQUENCE STUDENT_ID_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
-- Create sequence for students.
CREATE SEQUENCE STUDENT_VERSION_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
-- Create file table.
CREATE TABLE STUDENTS
(
  STUDENT_VERSION_ID BIGINT      NOT NULL DEFAULT NEXTVAL('STUDENT_VERSION_SEQUENCE'),
  STUDENT_ID         BIGINT      NOT NULL,
  NAME               VARCHAR(64) NOT NULL,
  LAST_NAME          VARCHAR(64) NOT NULL,
  CREATED            TIMESTAMP WITH TIME ZONE,
  CONSTRAINT STUDENT_PKEY PRIMARY KEY (STUDENT_VERSION_ID)
);

-- Create sequence for student properties.
CREATE SEQUENCE STUDENT_PROPERTY_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE STUDENT_PROPERTIES (
  PROPERTY_ID        BIGINT      NOT NULL DEFAULT NEXTVAL('STUDENT_PROPERTY_SEQUENCE'),
  STUDENT_VERSION_FK BIGINT      NOT NULL REFERENCES STUDENTS (STUDENT_VERSION_ID),
  NAME               VARCHAR(64) NOT NULL,
  VALUE              VARCHAR(512),
  ORDER_NUMBER       INTEGER              DEFAULT 0,
  REFERENCE_ID       BIGINT,
  CONSTRAINT STUDENT_PROPERTY_PKEY PRIMARY KEY (PROPERTY_ID)
);

CREATE TABLE STUDENTS_VERSION_MAPPING (
  STUDENT_FK         BIGINT NOT NULL UNIQUE,
  STUDENT_VERSION_FK BIGINT NOT NULL UNIQUE REFERENCES STUDENTS (STUDENT_VERSION_ID),
  CREATED            TIMESTAMP WITH TIME ZONE DEFAULT now(),
  UPDATED            TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT STUDENT_ID_PKEY PRIMARY KEY (STUDENT_FK)
);

CREATE OR REPLACE VIEW V_STUDENTS AS
  SELECT
    s.STUDENT_ID,
    s.STUDENT_VERSION_ID,
    s.NAME,
    s.LAST_NAME,
    s_id.CREATED,
    s_id.UPDATED
  FROM STUDENTS s, STUDENTS_VERSION_MAPPING s_id
  WHERE
    s.STUDENT_VERSION_ID = s_id.STUDENT_VERSION_FK;


-- UPDATE CURRENT STUDENT VERSION PROCEDURE --
CREATE OR REPLACE FUNCTION update_current_student_version()
  RETURNS TRIGGER
AS $update_student$
BEGIN
  IF EXISTS(SELECT 1
            FROM STUDENTS_VERSION_MAPPING
            WHERE STUDENTS_VERSION_MAPPING.student_fk = NEW.student_id)
  THEN
    UPDATE STUDENTS_VERSION_MAPPING
    SET STUDENT_VERSION_FK = NEW.STUDENT_VERSION_ID, UPDATED = NEW.CREATED
    WHERE STUDENT_FK = NEW.STUDENT_ID;
  ELSE
    INSERT INTO STUDENTS_VERSION_MAPPING (STUDENT_FK, STUDENT_VERSION_FK, CREATED, UPDATED) VALUES (NEW.STUDENT_ID, NEW.STUDENT_VERSION_ID, NEW.CREATED, NEW.CREATED);
  END IF;

  RETURN NEW;
END;
$update_student$ LANGUAGE plpgsql;

CREATE TRIGGER update_student
AFTER INSERT ON STUDENTS
FOR EACH ROW
EXECUTE PROCEDURE update_current_student_version();


-- Generate student id --
CREATE OR REPLACE FUNCTION getStudentId()
  RETURNS BIGINT AS
  $$
  BEGIN
    RETURN nextval('student_id_sequence');
  END;
  $$ LANGUAGE plpgsql;
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------




------------------------------------------------------------------------------------------------------
-------------------------------------- 0.0.4 ---------------------------------------------------------
------------------------------------------------------------------------------------------------------
CREATE SEQUENCE TRANSACTIONS_TYPE_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE TRANSACTION_TYPE
(
  TRANSACTION_TYPE_ID BIGINT DEFAULT nextval('TRANSACTIONS_TYPE_SEQUENCE')   NOT NULL,
  TYPE_CODE           VARCHAR(64)                                            NOT NULL,
  DESCRIPTION         VARCHAR(256),
  CONSTRAINT TRANSACTION_TYPE_PKEY PRIMARY KEY (TRANSACTION_TYPE_ID)
);

INSERT INTO TRANSACTION_TYPE (TRANSACTION_TYPE_ID, TYPE_CODE, DESCRIPTION) VALUES (DEFAULT, 'D1', 'Cash payment to increase student balance ');
INSERT INTO TRANSACTION_TYPE (TRANSACTION_TYPE_ID, TYPE_CODE, DESCRIPTION) VALUES (DEFAULT, 'D2', 'Bank payment to increase student balance');
INSERT INTO TRANSACTION_TYPE (TRANSACTION_TYPE_ID, TYPE_CODE, DESCRIPTION) VALUES (DEFAULT, 'K1', 'Automatic payment for participation in subject event');

CREATE SEQUENCE STUDENT_TRANSACTIONS_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE TABLE STUDENT_TRANSACTIONS
(
  TRANSACTION_ID      BIGINT DEFAULT nextval('STUDENT_TRANSACTIONS_SEQUENCE') NOT NULL,
  STUDENT_FK          BIGINT                                                  NOT NULL REFERENCES STUDENTS_VERSION_MAPPING (STUDENT_FK),
  DEBIT               INT DEFAULT 0                                           NOT NULL,
  CREDIT              INT DEFAULT 0                                           NOT NULL,
  DESCRIPTION         VARCHAR(256),
  TRANSACTION_TYPE_FK BIGINT                                                  NOT NULL REFERENCES TRANSACTION_TYPE (TRANSACTION_TYPE_ID),
  CREATED             TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT STUDENT_TRANSACTION_PKEY PRIMARY KEY (TRANSACTION_ID)
);

CREATE OR REPLACE VIEW V_STUDENT_BALANCE (STUDENT_FK, SUM) AS
  SELECT
    STUDENT_FK,
    (sum(DEBIT) - sum(CREDIT))
  FROM STUDENT_TRANSACTIONS
  GROUP BY STUDENT_FK;

------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------




------------------------------------------------------------------------------------------------------
-------------------------------------- 0.2.0 ---------------------------------------------------------
------------------------------------------------------------------------------------------------------
-- Create sequence for balance.
CREATE SEQUENCE STUDENT_DOCUMENTS_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Create file table.
CREATE TABLE STUDENT_DOCUMENTS
(
  DOCUMENT_ID BIGINT      NOT NULL     DEFAULT nextval('STUDENT_DOCUMENTS_SEQUENCE'),
  FILE_FK BIGINT NOT NULL REFERENCES files (FILE_ID),
  STUDENT_FK  BIGINT      NOT NULL REFERENCES students_version_mapping (STUDENT_FK),
  STATUS      VARCHAR(64) NOT NULL, --SAVED,DELETED
  CREATED     TIMESTAMP WITH TIME ZONE DEFAULT now(),
  UPDATED     TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT STUDENT_DOCUMENTS_PKEY PRIMARY KEY (DOCUMENT_ID)
);

CREATE OR REPLACE VIEW V_STUDENT_DOCUMENTS AS
  SELECT
    s.document_id,
    s.file_fk,
    s.student_fk,
    s.created,
    t.name,
    t.size
  FROM STUDENT_DOCUMENTS s, files t
  WHERE
    s.FILE_FK = t.file_id
    AND s.status = 'SAVED';
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------



------------------------------------------------------------------------------------------------------
-------------------------------------- 0.3.0 ---------------------------------------------------------
------------------------------------------------------------------------------------------------------
-- Create sequence for faktura id.
CREATE SEQUENCE FAKTURA_REPORT_ID_SEQUENCE START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE OR REPLACE FUNCTION getFakturaId()
  RETURNS BIGINT AS
  $$
  BEGIN
    RETURN nextval('FAKTURA_REPORT_ID_SEQUENCE');
  END;
  $$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------
