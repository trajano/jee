## Creating a mariadb database

    create database jeesample character set 'utf8mb4' collate utf8mb4_unicode_ci;
    create user 'jeeuser'@'localhost' identified by 'password';
    grant all privileges on jeesample.* to 'jeeuser'@'localhost';


