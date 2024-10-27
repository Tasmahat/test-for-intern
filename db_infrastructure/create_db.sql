CREATE DATABASE BankDeposits;

CREATE TABLE legal_form(
    legal_form_name varchar PRIMARY KEY
);

CREATE TABLE clients(
    client_name varchar PRIMARY KEY,
    short_client_name varchar NOT NULL,
    client_address varchar NOT NULL,
    legal_form_name varchar,
    FOREIGN KEY (legal_form_name) REFERENCES legal_form(legal_form_name)
);

CREATE TABLE banks(
    bank_name varchar NOT NULL,
    bank_bic varchar PRIMARY KEY
);

CREATE TABLE deposits(
    client_name varchar,
    bank_bic varchar,
    deposit_open_date date,
    deposit_percent decimal,
    deposit_term decimal,
    FOREIGN KEY (client_name) REFERENCES clients(client_name),
    FOREIGN KEY (bank_bic) REFERENCES banks(bank_bic),
    PRIMARY KEY (client_name, bank_bic, deposit_open_date)
);

INSERT INTO legal_form VALUES ('ИП'), ('ОАО'), ('ООО'), ('ЗАО'), ('Самозанятый');

INSERT INTO clients VALUES
('test', 'tst', 'test', 'ИП'),
('atest', 'btst', 'ctest', 'ОАО'),
('btest', 'ctst', 'atest', 'ООО');

INSERT INTO banks VALUES
('test', '123'),
('atest', '432'),
('btest', '932');

INSERT INTO deposits VALUES
('test', '123', 01-01-2000, 5, 40),
('atest', '932', 03-03-2001, 20, 3),
('btest', '123', 02-02-2003, 32, 3);