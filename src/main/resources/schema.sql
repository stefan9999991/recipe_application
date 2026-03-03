create table if not exists recipes (
    id serial not null,
    name varchar,
    description varchar,
    time integer,
    meal_type varchar,
    primary key (id)
);

create table if not exists ingredients (
    id serial not null,
    name varchar,
    amount varchar,
    recipe_id integer not null,
    primary key (id),
    constraint fk_recipe foreign key (recipe_id)
    references recipes(id)
);