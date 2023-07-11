CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT PRIMARY KEY,
    name  VARCHAR(255),
    email VARCHAR(512) UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id          BIGINT generated by default as identity PRIMARY KEY,
    description VARCHAR(200),
    requester   BIGINT REFERENCES users (id),
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT PRIMARY KEY,
    owner       BIGINT REFERENCES users (id),
    name        VARCHAR(20)  NOT NULL,
    description VARCHAR(200) NOT NULL,
    available   BOOLEAN      NOT NULL,
    request     BIGINT REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT generated by default as identity PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT REFERENCES items (id),
    booker_id  BIGINT REFERENCES users (id),
    status     VARCHAR
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT generated by default as identity PRIMARY KEY,
    text      VARCHAR(200),
    item_id   BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES users (id),
    created   TIMESTAMP WITHOUT TIME ZONE
);