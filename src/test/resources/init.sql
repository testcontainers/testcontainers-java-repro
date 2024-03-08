CREATE TABLE test_table (
    id SERIAL PRIMARY KEY,
    data VARCHAR(255) NOT NULL
);

INSERT INTO test_table (data) VALUES ('Test data 1');
INSERT INTO test_table (data) VALUES ('Test data 2');
INSERT INTO test_table (data) VALUES ('Test data 3');