create table if not exists transactions
(
    id      uuid  default random_uuid() primary key,
    amount  decimal,
    slogan varchar(255),
    timestamp timestamp,
    reference varchar(255),
    receiving_user varchar(255)

);
