DROP TABLE IF EXISTS tb_event;
DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_booking;

CREATE TABLE tb_user
(
    id       bigint(20) primary key not null auto_increment,
    email    varchar(100)           not null,
    password varchar(100)           not null
);

CREATE TABLE tb_event
(
    id          bigint(20) primary key not null auto_increment,
    title       varchar(100)           NOT NULL,
    description varchar(255)           NOT NULL,
    price       double                 NOT NULL,
    date        timestamp              NOT NULL default current_timestamp,
    creator_id  bigint(20) unsigned    not null,
    foreign key (creator_id) references tb_user (id)
);


CREATE TABLE tb_booking
(
    id           bigint(20) primary key not null auto_increment,
    user_id      bigint(20) unsigned    not null,
    event_id     bigint(20) UNSIGNED    not null,
    created_time timestamp              not null default current_timestamp,
    update_time  timestamp              not null default current_timestamp,
    foreign key (user_id) references tb_user (id),
    foreign key (event_id) references tb_event (id)
);

CREATE UNIQUE INDEX idx_user_id ON tb_booking (user_id);
