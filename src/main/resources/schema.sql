CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name varchar(50) NOT NULL,
                      password varchar(50) NOT NULL
);

CREATE TABLE account (
                         id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         name varchar(50) NOT NULL,
                         user_id BIGINT NOT NULL
);
CREATE TABLE user_account (
                              user_id BIGINT NOT NULL,
                              account_id BIGINT NOT NULL
);
CREATE TABLE category (
                          id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          name varchar(50) NOT NULL,
                          DESCRIPTION varchar(255)
);
CREATE TABLE recursion (
                           id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           date DATETIME NOT NULL
);
CREATE TABLE transaction (
                             id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                             account_id BIGINT NOT NULL,
                             name varchar(50) NOT NULL,
                             description varchar(255),
                             amount DOUBLE PRECISION NOT NULL,
                             date DATETIME NOT NULL,
                             recursion_id BIGINT NOT NULL,
                             type ENUM('EXPENSE', 'INCOME') NOT NULL
);
CREATE TABLE transaction_category (
                                      transaction_id BIGINT NOT NULL,
                                      category_id BIGINT NOT NULL
);
