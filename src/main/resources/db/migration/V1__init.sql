CREATE TABLE client (
  id          UUID NOT NULL PRIMARY KEY,
  first_name  TEXT NOT NULL,
  last_name   TEXT NOT NULL,
  email       TEXT,
  phone       TEXT,
  gender      TEXT NOT NULL,
  status      TEXT NOT NULL
);

CREATE TABLE appointment (
  id          UUID NOT NULL PRIMARY KEY,
  client_id   UUID NOT NULL REFERENCES client(id),
  start_time  TIMESTAMP NOT NULL,
  end_time    TIMESTAMP NOT NULL
);

CREATE TABLE item (
  id                UUID NOT NULL PRIMARY KEY,
  appointment_id    UUID NOT NULL REFERENCES appointment(id),
  name              TEXT NOT NULL,
  price             MONEY NOT NULL,
  type              TEXT NOT NULL,
  loyalty_points    INT NOT NULL
);