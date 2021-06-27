CREATE TABLE wallet (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      user_id VARCHAR NOT NULL,
      balance DECIMAL NOT NULL,
      currency VARCHAR NOT NULL check (currency in ('EUR', 'GBP')),
      last_updated TIMESTAMP DEFAULT now(),
      CONSTRAINT FK_WALLET_USER_ID FOREIGN KEY (user_id) REFERENCES security_user (id)
) ;

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    global_id VARCHAR UNIQUE NOT NULL,
    transaction_type VARCHAR NOT NULL check (transaction_type in ('C', 'D')),
    amount DECIMAL NOT NULL,
    wallet_id BIGINT NOT NULL,
    description VARCHAR NOT NULL,
    last_updated TIMESTAMP DEFAULT now(),
    CONSTRAINT FK_TRANSACTION_WALLET_ID FOREIGN KEY (wallet_id) REFERENCES wallet (id)
) ;