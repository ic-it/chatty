CREATE TABLE public.users (
    id              SERIAL      PRIMARY KEY     ,
    username        character   varying(255)    ,
    api_key         character   varying(255)    ,
    is_online       BOOLEAN
);

CREATE TABLE public.chats (
    id              SERIAL      PRIMARY KEY     ,
    name            character   varying(255)
);


CREATE TABLE public.messages (
    id              SERIAL      PRIMARY KEY     ,
    chat_id         INTEGER                     ,
    user_id         INTEGER                     ,
    text            character   varying(255)    ,
    time            INTEGER						,
    type			character   varying(255)
);


ALTER TABLE public.users           OWNER to "postgres";
ALTER TABLE public.messages        OWNER to "postgres";

