CREATE TYPE user_role AS ENUM ('ADMIN', 'ANALYST');
CREATE TYPE rule_severity AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role user_role NOT NULL
);

CREATE TABLE detection_rules (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 name VARCHAR(255) NOT NULL,
                                 description TEXT,
                                 condition_logic TEXT NOT NULL,
                                 severity rule_severity NOT NULL,
                                 is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                token VARCHAR(500) NOT NULL UNIQUE,
                                token_id VARCHAR(255) NOT NULL UNIQUE,
                                username VARCHAR(255) NOT NULL,
                                expires_at TIMESTAMP NOT NULL,
                                issued_at TIMESTAMP NOT NULL,
                                revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                revoked_at TIMESTAMP,
                                ip_address VARCHAR(45),
                                user_agent VARCHAR(500),
                                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE security_logs (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               username VARCHAR(255) NOT NULL,
                               action VARCHAR(50) NOT NULL,
                               ip_address VARCHAR(45) NOT NULL,
                               status VARCHAR(50) NOT NULL,
                               details TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_username ON refresh_tokens(username);
CREATE INDEX idx_refresh_tokens_token_id ON refresh_tokens(token_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);
CREATE INDEX idx_security_logs_username ON security_logs(username);
CREATE INDEX idx_security_logs_created_at ON security_logs(created_at);