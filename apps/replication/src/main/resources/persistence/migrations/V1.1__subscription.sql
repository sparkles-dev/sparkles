create table SubscriptionEntity (
  id binary(16) not null,
  notify_url varchar(255) not null,
  topic varchar(255) not null,
  primary key (id)
)
