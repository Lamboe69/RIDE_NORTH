-- Driver applications table
-- Pre-login onboarding: drivers apply with personal + vehicle details,
-- admin reviews and approves, then the driver receives their login details by SMS.
CREATE TABLE IF NOT EXISTS driver_applications (
    id UUID PRIMARY KEY,
    application_ref VARCHAR(30) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    nin_number VARCHAR(20),
    license_number VARCHAR(30) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    plate_number VARCHAR(20) NOT NULL,
    make VARCHAR(100),
    model VARCHAR(100),
    year VARCHAR(20),
    capacity INTEGER NOT NULL DEFAULT 1,
    documents VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    rejection_reason VARCHAR(500),
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_app_phone ON driver_applications(phone_number);
CREATE INDEX idx_app_status ON driver_applications(status);
CREATE INDEX idx_app_ref ON driver_applications(application_ref);
