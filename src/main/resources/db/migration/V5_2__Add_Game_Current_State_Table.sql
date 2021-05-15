create table game_current_state(
                                 id serial not null primary key ,
                                 time_over boolean,
                                 created_at timestamp not null ,
                                 game_id integer not null ,
                                 members jsonb
);