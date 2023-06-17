DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR,
    email   VARCHAR UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR,
    requestor   BIGINT REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR,
    description  VARCHAR,
    is_available BOOLEAN,
    owner_id     BIGINT REFERENCES users (user_id),
    request_id   BIGINT REFERENCES requests (request_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP,
    end_date   TIMESTAMP,
    item_id    BIGINT REFERENCES items (item_id),
    booker_id  BIGINT REFERENCES users (user_id),
    status     VARCHAR
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text       VARCHAR,
    item_id    BIGINT REFERENCES items (item_id),
    author_id  BIGINT REFERENCES users (user_id)
);