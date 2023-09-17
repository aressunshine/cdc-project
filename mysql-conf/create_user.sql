show variables like 'log_%';

show variables like '%format%';

SHOW GRANTS FOR 'root'@'localhost'

create user 'debezium'@'%' identified by 'debezium';

grant select,reload,show databases,replication slave,replication client on *.* to 'debezium'@'%' with grant option;

flush privileges;

SHOW GRANTS FOR 'debezium'@'%'