CREATE TABLE IF NOT EXISTS cash_user (
            id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
            name varchar(50) NOT NULL,
            password varchar(100) NOT NULL,
            enabled BOOLEAN NOT NULL DEFAULT true,
            account_non_expired BOOLEAN NOT NULL DEFAULT true,
            account_non_locked BOOLEAN NOT NULL DEFAULT true,
            credentials_non_expired BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS authority(
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_authority(
           user_id BIGINT,
           authority_id BIGINT,
           FOREIGN KEY (user_id) REFERENCES cash_user(id),
           FOREIGN KEY (authority_id) REFERENCES authority(id),
           PRIMARY KEY (user_id, authority_id)
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

CREATE TABLE IF NOT EXISTS transaction (
                                           id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                           account_id BIGINT NOT NULL,
                                           name varchar(50) not null,
                                           description varchar(255),
                                           amount double precision not null,
                                           date DATETIME not null,
                                           type ENUM('EXPENSE', 'INCOME') NOT NULL,

                                           FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recursion (
                                         id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                         transaction_id BIGINT NOT NULL UNIQUE,
                                         date DATETIME NOT NULL,

                                         FOREIGN KEY (transaction_id) REFERENCES transaction(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS transaction_category (
                                                    transaction_id BIGINT NOT NULL,
                                                    category_id BIGINT NOT NULL,

                                                    FOREIGN KEY (transaction_id) REFERENCES transaction(id) ON DELETE CASCADE,
                                                    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
                                                    PRIMARY KEY(transaction_id, category_id)
);