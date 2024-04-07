CREATE TABLE IF NOT EXISTS cash_user (
                                         id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                         name varchar(50) NOT NULL,
                                         password varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS account (
                                       id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                       name varchar(50) NOT NULL,
                                       user_id BIGINT NOT NULL,

                                       FOREIGN KEY (user_id) REFERENCES cash_user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_account (
                                            user_id BIGINT NOT NULL,
                                            account_id BIGINT NOT NULL,

                                            FOREIGN KEY (user_id) REFERENCES cash_user(id) ON DELETE CASCADE,
                                            FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
                                            PRIMARY KEY(user_id, account_id)
);

CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        name varchar(50) NOT NULL,
                                        description varchar(255)
);

CREATE TABLE IF NOT EXISTS recursion (
                                         id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                         date DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
                                           id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                           account_id BIGINT NOT NULL,
                                           name varchar(50) not null,
                                           description varchar(255),
                                           amount double precision not null,
                                           date DATETIME not null,
                                           recursion_id BIGINT NOT NULL,
                                           type ENUM('EXPENSE', 'INCOME') NOT NULL,

                                           FOREIGN KEY (recursion_id) REFERENCES recursion(id) ON DELETE CASCADE,
                                           FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transaction_category (
                                                    transaction_id BIGINT NOT NULL,
                                                    category_id BIGINT NOT NULL,

                                                    FOREIGN KEY (transaction_id) REFERENCES transaction(id) ON DELETE CASCADE,
                                                    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
                                                    PRIMARY KEY(transaction_id, category_id)
);