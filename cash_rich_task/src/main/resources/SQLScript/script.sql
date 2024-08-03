-- Create the sequence for AccountUserProfile table starting at 1
CREATE SEQUENCE account_user_profile_seq START 1;

-- Create the table for AccountUserProfile
CREATE TABLE account_user_profile (
    id BIGINT PRIMARY KEY DEFAULT nextval('account_user_profile_seq'),
    username VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL
);

-- Create the sequence for AccountUser table starting at 100
CREATE SEQUENCE account_user_seq START 100;

-- Create the table for AccountUser
CREATE TABLE account_user (
    id BIGINT PRIMARY KEY DEFAULT nextval('account_user_seq'),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    mobile VARCHAR(15) NOT NULL,
    account_user_profile_id BIGINT,
    CONSTRAINT fk_account_user_profile
        FOREIGN KEY (account_user_profile_id)
        REFERENCES account_user_profile(id)
);

-- Create the sequence for Coin table starting at 1000
CREATE SEQUENCE coin_seq START 1000;

-- Create the table for Coin
CREATE TABLE coin (
    id BIGINT PRIMARY KEY DEFAULT nextval('coin_seq'),
    user_id VARCHAR(255) NOT NULL,
    request_url VARCHAR(255) NOT NULL,
    request_payload TEXT,
    response_payload TEXT,
    timestamp TIMESTAMP NOT NULL
);
