create table Drink (
  id binary(16) not null,
  created_at timestamp not null,
  created_by varchar(255) not null,
  name varchar(255) not null,
  primary key (id)
)
