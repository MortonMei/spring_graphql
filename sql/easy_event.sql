DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user {
    id serial,
    email varchar(100) not null,
    password varchar(100) not null,
    primary key(id)
};

CREATE TABLE event {
    id serial,
    title varchar(100) NOT NULL,
    description varchar(255) NOT NULL,
    price double NOT NULL,
    date timestamp NOT NULL,
    creator_id integer not null,
    primary key(id)
    constraint fk_created_id foreign key (creator_id) references tb_user(id)
};
