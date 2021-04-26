DROP table games_courses;
DROP table games;
CREATE table games(
                      id bigserial not null primary key ,
                      title varchar(256) not null ,
                      subject_name varchar(256) not null,
                      course_id bigserial not null,
                      constraint game_course_fk foreign key (course_id) references courses(id)
);