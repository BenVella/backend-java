CREATE DATABASE keycloak_db;
CREATE DATABASE app_db;

GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO admin;
GRANT ALL PRIVILEGES ON DATABASE app_db TO admin;

-- Create a new user for Keycloak
CREATE USER keycloak_service WITH PASSWORD 'keycloak_password';

-- Grant the necessary permissions to the keycloak_service user on keycloak_db
GRANT CONNECT ON DATABASE keycloak_db TO keycloak_service;
GRANT USAGE ON SCHEMA public TO keycloak_service;
GRANT CREATE, SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO keycloak_service;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO keycloak_service;

-- Ensure future tables and sequences also have the correct permissions
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO keycloak_service;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO keycloak_service;