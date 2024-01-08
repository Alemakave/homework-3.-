CREATE TABLE car
(
    id BIGINT PRIMARY KEY,
    mark TEXT,
    model TEXT,
    cost INTEGER
);

CREATE TABLE car_user
(
    id BIGINT PRIMARY KEY,
    name TEXT,
    age INTEGER,
    isDriver BOOLEAN,
    car_id BIGINT REFERENCES car (id)
);


DROP TABLE car_user;

DROP TABLE car;
