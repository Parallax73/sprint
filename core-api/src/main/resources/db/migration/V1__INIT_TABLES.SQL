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