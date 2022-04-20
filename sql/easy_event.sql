DROP TABLE IF EXISTS event;
CREATE TABLE event {
    id serial,
    title varchar(100) NOT NULL,
    description varchar(255) NOT NULL,
    price double NOT NULL,
    date timestamp NOT NULL,
    primary key(id)
}