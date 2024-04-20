-- Inserare în tabelul cashUser
INSERT INTO cash_user (name, password) VALUES
                                           ('Alice', 'pass1'),
                                           ('Bob', 'pass2'),
                                           ('Charlie', 'pass3'),
                                           ('David', 'pass4'),
                                           ('Emma', 'pass5'),
                                           ('Fiona', 'pass6'),
                                           ('Fiona', 'pass10'),
                                           ('George', 'pass7');

-- Inserare în tabelul account (asociind fiecare cont unui utilizator)
INSERT INTO account (name, user_id) VALUES
                                        ('Savings Account', 1),
                                        ('Checking Account', 2),
                                        ('Credit Card', 3),
                                        ('Investment Account', 4),
                                        ('Loan Account', 5),
                                        ('Retirement Account', 6),
                                        ('Business Account', 7);

-- Inserare în tabelul user_account (asociind fiecare utilizator cu fiecare cont)
INSERT INTO user_account (user_id, account_id) VALUES
                                                   (1, 1), (1, 2),
                                                   (2, 1), (2, 2), (2, 3),
                                                   (3, 1), (3, 2), (3, 5), (3, 7),
                                                   (4, 1), (4, 2),
                                                   (5, 1), (5, 3), (5, 6),
                                                   (6, 1), (6, 2),
                                                   (7, 1), (7, 2), (7, 3), (7, 4);

-- Inserare în tabelul category
INSERT INTO category (name, DESCRIPTION) VALUES
                                             ('Food', 'Expenses related to food purchases'),
                                             ('Transportation', 'Expenses related to transportation'),
                                             ('Utilities', 'Expenses related to utility bills'),
                                             ('Entertainment', 'Expenses related to entertainment activities'),
                                             ('Salary', 'Income from salary'),
                                             ('Investment', 'Income from investments'),
                                             ('Gifts', 'Expenses related to gifts and donations');



-- Inserare în tabelul transaction
INSERT INTO transaction (account_id, name, description, amount, date, type) VALUES
                                                                                              (1, 'Groceries', 'Weekly grocery shopping', 150.50, '2024-03-01', 'EXPENSE'),
                                                                                              (1, 'Dinner', 'Dinner with friends', 50.00, '2024-03-02', 'EXPENSE'),
                                                                                              (2, 'Salary', 'Monthly salary', 2500.00, '2024-03-03', 'INCOME'),
                                                                                              (2, 'Gasoline', 'Fuel for car', 70.25, '2024-03-04', 'EXPENSE'),
                                                                                              (3, 'Electricity Bill', 'Monthly electricity bill', 120.00, '2024-03-05', 'EXPENSE'),
                                                                                              (3, 'Netflix Subscription', 'Monthly subscription', 15.99, '2024-03-06', 'EXPENSE'),
                                                                                              (4, 'Stock Purchase', 'Investment in stocks', 500.00, '2024-03-07', 'EXPENSE');

-- Inserare în tabelul recursion
INSERT INTO recursion (transaction_id, date) VALUES
                                                 (2, '2024-03-01'), (3, '2024-03-02'), (4, '2024-03-03'), (5, '2024-03-04'),
                                                 (6, '2024-03-05');

-- Inserare în tabelul transaction_category
INSERT INTO transaction_category (transaction_id, category_id) VALUES
                                                                      (1, 1), (2, 4), (3, 5), (4, 2), (5, 3), (6, 4), (7, 6);
