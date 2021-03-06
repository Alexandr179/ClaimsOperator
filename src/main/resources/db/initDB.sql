DROP TABLE IF EXISTS claim_state;
DROP TABLE IF EXISTS user_roles;

DROP TABLE IF EXISTS claim;
DROP TABLE IF EXISTS token;
DROP TABLE IF EXISTS cl_user;
DROP TABLE IF EXISTS persistent_logins;

-- Created DB as handle, as Hibernate (by UPDATE)
-- table  persistent_logins   and  token   - created only Hibernate (by UPDATE)

CREATE TABLE cl_user
(
    id            int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name    VARCHAR,
    email         VARCHAR,
    hash_password VARCHAR,
    role          VARCHAR
);
-- CREATE UNIQUE INDEX users_unique_email_idx ON cl_user (email);


CREATE TABLE claim
(
    id           int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text         VARCHAR(1020),
    state        VARCHAR,
    created_time TIMESTAMP DEFAULT now() NOT NULL,
    user_id      INTEGER,

    FOREIGN KEY (user_id) REFERENCES cl_user (id)
);
-- CREATE UNIQUE INDEX claims_unique_user_datetime_idx ON claim (id, created_time);
