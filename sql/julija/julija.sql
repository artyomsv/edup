-- Table: user_accounts
-- DROP TABLE user_accounts;
CREATE TABLE user_accounts (
  user_account_id                    BIGINT NOT NULL,
  user_account_name                  VARCHAR(64),
  user_account_surname               VARCHAR(64),
  user_account_owner                 BIGINT,
  user_account_type                  INT,
  user_account_identification_number BIGINT,
  user_account_email                 VARCHAR(64),
  user_account_parents_info          VARCHAR(64),
  user_account_photo                 LINE,
  user_account_balance               BIGINT,
  user_account_registration_date     DATE,
  CONSTRAINT user_account_id PRIMARY KEY (user_account_id),
  CONSTRAINT user_accounts_user_account_owner_fkey FOREIGN KEY (user_account_owner)
  REFERENCES users (user_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: user_account_transaction_history
-- DROP TABLE user_account_transaction_history;
CREATE TABLE user_account_transaction_history (
  user_account_transaction_history_id     BIGINT NOT NULL,
  user_account_id                         BIGINT,
  user_account_history_debit              BIGINT,
  user_account_history_credit             BIGINT,
  user_account_currency                   CHARACTER(1),
  user_account_transaction_history_amount BIGINT,
  user_account_transaction_date           DATE,
  CONSTRAINT user_account_history_pkey PRIMARY KEY (user_account_transaction_history_id),
  CONSTRAINT user_account_history_user_account_id_fkey FOREIGN KEY (user_account_id)
  REFERENCES user_accounts (user_account_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: user_account_history
-- DROP TABLE user_account_history;
CREATE TABLE user_account_history (
  user_account_id       BIGINT,
  user_account_event_id BIGINT,
  user_account_date     DATE,
  user_account_status   BOOLEAN
);

-- Table: subjects
-- DROP TABLE subjects;
CREATE TABLE subjects (
  subject_id           BIGINT NOT NULL,
  subject_name         TEXT,
  subject_user_account BIGINT,
  CONSTRAINT subjects_pkey PRIMARY KEY (subject_id),
  CONSTRAINT subjects_subject_user_account_fkey FOREIGN KEY (subject_user_account)
  REFERENCES user_accounts (user_account_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: hours
-- DROP TABLE hours;
CREATE TABLE hours (
  hours_id               BIGINT,
  user_account_id        BIGINT,
  subject_id             BIGINT,
  subject_date           DATE,
  subject_hours_total    BIGINT,
  subject_price_per_hour BIGINT,
  subject_total_price    BIGINT
);

-- Table: calendar
-- DROP TABLE calendar;
CREATE TABLE calendar (
  calendar_id         BIGINT NOT NULL,
  calendar_event_id   BIGINT,
  calendar_owner_id   BIGINT,
  calendar_event_name TEXT,
  CONSTRAINT calendar_id PRIMARY KEY (calendar_id)
);