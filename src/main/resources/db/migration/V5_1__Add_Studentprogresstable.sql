create table student_progress(
    id serial not null primary key ,
    student_id integer not null ,
    game_id integer not null ,
    taken_tasks jsonb
);