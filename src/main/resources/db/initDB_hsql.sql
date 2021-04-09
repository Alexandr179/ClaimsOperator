DROP TABLE cl_user IF EXISTS;
DROP TABLE claim IF EXISTS;

CREATE TABLE cl_user
(
    id           INTEGER PRIMARY KEY,
    firstName    VARCHAR(255),
    email        VARCHAR(255),
    hashPassword VARCHAR(255),
    authority    VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON cl_user (email);


CREATE TABLE claim
(
    id          INTEGER PRIMARY KEY,
    text        VARCHAR(1020),
    state  VARCHAR(255)            NOT NULL,
    createdTime TIMESTAMP DEFAULT now() NOT NULL,
    user_id     INTEGER,

    FOREIGN KEY (user_id) REFERENCES cl_user (id)
);
CREATE UNIQUE INDEX claims_unique_user_datetime_idx ON claim (user_id, createdTime)

