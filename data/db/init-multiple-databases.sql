CREATE DATABASE keycloak_db;
CREATE DATABASE app_db;

-- Keycloak DB Service User
-- Create a new user for Keycloak
CREATE USER keycloak_service WITH ENCRYPTED PASSWORD 'keycloak_password';

GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak_service;
-- See https://stackoverflow.com/a/75876944/2179720 for details on why we connect first
\c keycloak_db postgres
GRANT ALL ON SCHEMA public TO keycloak_service;

-- APP DB Service User
-- Create a new user for the application
CREATE USER app_service WITH ENCRYPTED PASSWORD 'app_password';

-- Grant the necessary permissions to the app_user on app_db
GRANT ALL PRIVILEGES ON DATABASE app_db TO app_service;
\c app_db postgres
GRANT ALL ON SCHEMA public TO app_service;