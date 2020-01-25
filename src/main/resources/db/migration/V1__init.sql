CREATE TABLE client (
  id          UUID NOT NULL PRIMARY KEY,
  first_name  TEXT,
  last_name   TEXT,
  email       TEXT,
  phone       TEXT,
  gender      TEXT,
  status      TEXT
);