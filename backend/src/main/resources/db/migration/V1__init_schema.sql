-- RideNorth Database Schema
-- Version 0.1.0

-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    nin_number VARCHAR(20),
    rating_avg DOUBLE PRECISION NOT NULL DEFAULT 5.0,
    rating_count INTEGER NOT NULL DEFAULT 0,
    profile_photo_url VARCHAR(500),
    preferred_language VARCHAR(2) NOT NULL DEFAULT 'en',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_role ON users(role);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id),
    type VARCHAR(20) NOT NULL,
    plate_number VARCHAR(20) NOT NULL,
    capacity INTEGER NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    photos VARCHAR(1000),
    make VARCHAR(100),
    model VARCHAR(100),
    year VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_vehicles_type ON vehicles(type);
CREATE INDEX idx_vehicles_owner ON vehicles(owner_id);

-- Driver profiles table
CREATE TABLE IF NOT EXISTS driver_profiles (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL REFERENCES users(id),
    license_number VARCHAR(30) NOT NULL,
    kyc_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    verification_docs VARCHAR(1000),
    is_online BOOLEAN NOT NULL DEFAULT FALSE,
    current_location GEOGRAPHY(Point, 4326),
    last_location_update TIMESTAMP,
    total_trips INTEGER NOT NULL DEFAULT 0,
    acceptance_rate DOUBLE PRECISION NOT NULL DEFAULT 100.0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_driver_user ON driver_profiles(user_id);
CREATE INDEX idx_driver_kyc ON driver_profiles(kyc_status);
CREATE INDEX idx_driver_online ON driver_profiles(is_online);

-- Ride requests table
CREATE TABLE IF NOT EXISTS ride_requests (
    id UUID PRIMARY KEY,
    rider_id UUID NOT NULL REFERENCES users(id),
    pickup_location GEOGRAPHY(Point, 4326) NOT NULL,
    dropoff_location GEOGRAPHY(Point, 4326) NOT NULL,
    pickup_address VARCHAR(200) NOT NULL,
    dropoff_address VARCHAR(200) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    passenger_count INTEGER NOT NULL DEFAULT 1,
    fare_estimate DOUBLE PRECISION NOT NULL,
    surge_multiplier DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    notes VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_ride_rider ON ride_requests(rider_id);
CREATE INDEX idx_ride_status ON ride_requests(status);

-- Trips table
CREATE TABLE IF NOT EXISTS trips (
    id UUID PRIMARY KEY,
    ride_request_id UUID UNIQUE NOT NULL REFERENCES ride_requests(id),
    rider_id UUID NOT NULL REFERENCES users(id),
    driver_id UUID NOT NULL REFERENCES driver_profiles(id),
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    distance_km DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    duration_minutes INTEGER NOT NULL DEFAULT 0,
    final_fare DOUBLE PRECISION NOT NULL,
    commission_amount DOUBLE PRECISION NOT NULL,
    driver_earnings DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'STARTED',
    cancellation_reason VARCHAR(500),
    payment_method VARCHAR(20) NOT NULL DEFAULT 'CASH',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_trip_rider ON trips(rider_id);
CREATE INDEX idx_trip_driver ON trips(driver_id);
CREATE INDEX idx_trip_status ON trips(status);

-- Freight jobs table
CREATE TABLE IF NOT EXISTS freight_jobs (
    id UUID PRIMARY KEY,
    shipper_id UUID NOT NULL REFERENCES users(id),
    pickup_location GEOGRAPHY(Point, 4326) NOT NULL,
    dropoff_location GEOGRAPHY(Point, 4326) NOT NULL,
    pickup_address VARCHAR(200) NOT NULL,
    dropoff_address VARCHAR(200) NOT NULL,
    cargo_type VARCHAR(20) NOT NULL,
    estimated_weight_kg DOUBLE PRECISION NOT NULL,
    estimated_volume_m3 DOUBLE PRECISION NOT NULL,
    cargo_description VARCHAR(500),
    preferred_date TIMESTAMP,
    preferred_vehicle_type VARCHAR(20),
    min_price DOUBLE PRECISION NOT NULL,
    max_price DOUBLE PRECISION NOT NULL,
    special_instructions VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_freight_shipper ON freight_jobs(shipper_id);
CREATE INDEX idx_freight_status ON freight_jobs(status);

-- Freight quotes table
CREATE TABLE IF NOT EXISTS freight_quotes (
    id UUID PRIMARY KEY,
    freight_job_id UUID NOT NULL REFERENCES freight_jobs(id),
    driver_id UUID NOT NULL REFERENCES driver_profiles(id),
    quoted_price DOUBLE PRECISION NOT NULL,
    estimated_duration_hours DOUBLE PRECISION NOT NULL,
    message VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_quote_job ON freight_quotes(freight_job_id);
CREATE INDEX idx_quote_driver ON freight_quotes(driver_id);
CREATE INDEX idx_quote_status ON freight_quotes(status);

-- Scheduled routes table
CREATE TABLE IF NOT EXISTS scheduled_routes (
    id UUID PRIMARY KEY,
    operator_id UUID NOT NULL REFERENCES users(id),
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    origin_location GEOGRAPHY(Point, 4326),
    destination_location GEOGRAPHY(Point, 4326),
    departure_time TIMESTAMP NOT NULL,
    seat_capacity INTEGER NOT NULL,
    seats_booked INTEGER NOT NULL DEFAULT 0,
    price_per_seat DOUBLE PRECISION NOT NULL,
    min_seats_to_confirm INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_route_operator ON scheduled_routes(operator_id);
CREATE INDEX idx_route_status ON scheduled_routes(status);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    payer_id UUID NOT NULL REFERENCES users(id),
    payee_id UUID NOT NULL REFERENCES users(id),
    trip_id UUID REFERENCES trips(id),
    freight_job_id UUID REFERENCES freight_jobs(id),
    scheduled_route_id UUID REFERENCES scheduled_routes(id),
    amount DOUBLE PRECISION NOT NULL,
    commission_amount DOUBLE PRECISION NOT NULL,
    net_amount DOUBLE PRECISION NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    provider_transaction_id VARCHAR(100),
    failure_reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_payment_payer ON payments(payer_id);
CREATE INDEX idx_payment_payee ON payments(payee_id);
CREATE INDEX idx_payment_status ON payments(status);
CREATE INDEX idx_payment_method ON payments(payment_method);

-- Wallets table
CREATE TABLE IF NOT EXISTS wallets (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL REFERENCES users(id),
    balance NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    pending_balance NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'UGX',
    last_updated TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_wallet_user ON wallets(user_id);

-- Ratings table
CREATE TABLE IF NOT EXISTS ratings (
    id UUID PRIMARY KEY,
    trip_id UUID UNIQUE NOT NULL REFERENCES trips(id),
    rater_id UUID NOT NULL REFERENCES users(id),
    ratee_id UUID NOT NULL REFERENCES users(id),
    score INTEGER NOT NULL,
    comment VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_rating_trip ON ratings(trip_id);
CREATE INDEX idx_rating_rater ON ratings(rater_id);
CREATE INDEX idx_rating_ratee ON ratings(ratee_id);

-- SOS events table
CREATE TABLE IF NOT EXISTS sos_events (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    trip_id UUID REFERENCES trips(id),
    location GEOGRAPHY(Point, 4326) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    notes VARCHAR(500),
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_sos_user ON sos_events(user_id);
CREATE INDEX idx_sos_trip ON sos_events(trip_id);
CREATE INDEX idx_sos_status ON sos_events(status);

-- USSD sessions table
CREATE TABLE IF NOT EXISTS ussd_sessions (
    id UUID PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL,
    session_state VARCHAR(50) NOT NULL,
    session_data TEXT NOT NULL DEFAULT '{}',
    last_action VARCHAR(100) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_ussd_phone ON ussd_sessions(phone_number);
CREATE INDEX idx_ussd_state ON ussd_sessions(session_state);

-- Insert default admin user (phone: 0700000000, will be activated via OTP)
INSERT INTO users (id, phone_number, name, role, created_at, updated_at, is_deleted, is_active)
VALUES ('a0000000-0000-0000-0000-000000000001', '0700000000', 'System Admin', 'ADMIN', NOW(), NOW(), FALSE, TRUE)
ON CONFLICT (phone_number) DO NOTHING;
