create table PERSON (
    ID int not null,
    NAME varchar(100) not null
);


create table EMPLOYEE (
    ID int not null,
    FIRSTNAME varchar(100) not null, 
    LASTNAME varchar(100) not null,
    EMAIL varchar(50) not null,
    primary key (ID)
);