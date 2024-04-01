CREATE TABLE users (
                      id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      name varchar(50) NOT NULL,
                      password varchar(50) NOT NULL
);

CREATE TABLE account (
                         id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         name varchar(50) NOT NULL,
                         user_id BIGINT NOT NULL,

                         FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE user_account (
                              user_id BIGINT NOT NULL,
                              account_id BIGINT NOT NULL,

                              FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                              FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
                              PRIMARY KEY(user_id, account_id)
);

CREATE TABLE category (
                          id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          name varchar(50) NOT NULL,
                          description varchar(255)
);

CREATE TABLE recursion (
                           id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           date DATETIME NOT NULL
);

CREATE TABLE transaction (
                             id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                             account_id BIGINT NOT NULL,
                             name varchar(50) not null,
                             description varchar(255),
                             amount double precision not null,
                             date DATETIME not null,
                             recursion_id BIGINT NOT NULL,
                             type VARCHAR(5) NOT NULL,

                             FOREIGN KEY (recursion_id) REFERENCES recursion(id) ON DELETE CASCADE,
                             FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

CREATE TABLE transaction_category (
                                      transaction_id BIGINT NOT NULL,
                                      category_id BIGINT NOT NULL,

                                      FOREIGN KEY (transaction_id) REFERENCES transaction(id) ON DELETE CASCADE,
                                      FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
                                      PRIMARY KEY(transaction_id, category_id)
);

alter table transaction modify column type ENUM('USD', 'EUR', 'GBP');