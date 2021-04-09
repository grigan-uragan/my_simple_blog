create table user_subscriptions (
    chanel_id int8 not null references users(id),
    subscriber_id int8 not null references users(id),
    primary key (chanel_id, subscriber_id)
)