create table users(
                      id bigserial primary key,
                      DB_TYPE varchar(4),
                      email varchar(255) not null ,
                      full_name varchar(255) not null ,
                      password text,
                      created_at timestamp not null default now(),
                      photo varchar(255)
);

create unique index users_email_uindex on users(email);

create table authorities(
                            id serial primary key ,
                            value varchar(255) not null
);

create unique index authorities_value_uindex on authorities(value);

create table user_authorities(
                                 user_id bigserial not null ,
                                 authority_id serial not null ,
                                 primary key (user_id, authority_id),
                                 constraint user_authorities_users_fk foreign key (user_id) references users(id),
                                 constraint user_authorities_authorities_fk foreign key (authority_id) references authorities(id)

);


insert into authorities (value) values ('ROLE_STUDENT');
insert into authorities (value) values ('ROLE_ADMIN');
insert into authorities (value) values ('ROLE_TEACHER');


CREATE table courses(
                      id bigserial not null primary key ,
                      title varchar(256) not null ,
                      teacher_id bigserial not null,
                      constraint course_teacher_fk foreign key (teacher_id) references users(id)
);

create table courses_students(
                                 student_id bigserial not null,
                                 course_id bigserial not null,
                                 primary key (student_id, course_id),
                                 constraint user_courses_fk foreign key (student_id) references users(id),
                                 constraint course_users_fk foreign key (course_id) references courses(id)
);


CREATE table games(
                             id bigserial not null primary key ,
                             title varchar(256) not null ,
                             subject_name varchar(256) not null
);


CREATE table games_courses(
                              game_id bigserial not null,
                              course_id bigserial not null,
                              primary key (game_id, course_id),
                              constraint game_courses_fk foreign key (game_id) references games(id),
                              constraint course_games_fk foreign key (course_id) references courses(id)
);

create table game_record(
                            student_id bigserial not null,
                            course_id bigserial not null,
                            date_time timestamp,
                            student_score integer default 0,
                            max_game_score integer,
                            primary key (student_id, course_id),
                            constraint user_courses_fk foreign key (student_id) references users(id),
                            constraint course_users_fk foreign key (course_id) references courses(id)
);