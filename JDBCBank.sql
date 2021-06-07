create table user_table( 
	user_id serial primary key,
	user_fname varchar(50),
	user_lname varchar(50),
	user_username varchar(50) unique, -- remember to add unique to username
	user_password varchar(50),
	user_status int default 0
);

create table account_table(
	account_id serial primary key,
	account_balance numeric(10, 2),
	account_type varchar(20)
);

create table user_account_join_table(
	user_id int references user_table(user_id),
	account_id int references account_table(account_id)
);

alter table account_table add constraint check_positive_balance check (account_balance >= 0);



--- for inserting accounts and users
--insert into user_account_join_table(user_id, account_id)
--values(11, 38);
--
--insert into account_table(account_balance, account_type)
--values(500, 'Checking');