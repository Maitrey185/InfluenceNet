-- Create additional databases if needed
CREATE DATABASE keycloak;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE influencenet TO influencenet_user;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO influencenet_user;

-- Connect to influencenet database
\c influencenet;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create initial schema
CREATE SCHEMA IF NOT EXISTS influencenet;

-- Set search path
ALTER DATABASE influencenet SET search_path TO influencenet, public;
