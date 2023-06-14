
SET FOREIGN_KEY_CHECKS = 0;

create database employee_db;

CREATE table employee_db.Employees(
 id int auto_increment,
 first_name varchar(30),
 last_name varchar(30),
 email varchar(50),
 primary key(id)
)

CREATE table employee_db.Addresses(
    id int AUTO_INCREMENT,
    type varchar(30),
    line1 varchar(50),
    line2 varchar(50),
    city varchar(50), 
    state varchar(50), 
    postal_code varchar(50),
    employee_id int,
    primary key(id)
)