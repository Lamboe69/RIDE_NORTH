# RideNorth - Unified Mobility & Logistics Platform for Northern Uganda

A mobile-first mobility and logistics platform purpose-built for Northern Uganda, covering passenger transport, agricultural freight, and cargo haulage.

## Architecture

```
RideNorth/
├── backend/                    # Spring Boot 3.x modular monolith (Kotlin)
│   ├── src/main/kotlin/com/ridenorth/
│   │   ├── config/            # Security, WebSocket, app config
│   │   ├── security/          # JWT auth filter & token provider
│   │   ├── common/            # Base entity, DTOs
│   │   └── module/
│   │       ├── auth/          # OTP-based phone auth
│   │       ├── user/          # Users, ratings, SOS events
│   │       ├── driver/        # Driver profiles, vehicles
│   │       ├── booking/       # Ride requests, trips
│   │       ├── freight/       # Freight jobs, quotes
│   │       ├── scheduled/     # Scheduled routes, seat booking
│   │       ├── payment/       # Payments, wallets
│   │       ├── matching/      # Driver matching engine
│   │       ├── pricing/       # Fare calculation
│   │       ├── notification/  # SMS/USSD via Africa's Talking
│   │       └── location/      # Real-time location updates
│   └── src/main/resources/
│       └── db/migration/      # Flyway SQL migrations
├── rider-app/                  # Android app (Kotlin + Jetpack Compose)
├── driver-app/                 # Android app (Kotlin + Jetpack Compose)
├── admin-dashboard/            # React + TypeScript + Vite
└── docker-compose.yml          # PostgreSQL + PostGIS, Redis, Backend
```

## Technology Stack

### Backend
- **Spring Boot 3.x** (Kotlin 1.9, Java 21)
- **PostgreSQL 16 + PostGIS** - Geospatial driver matching
- **Redis** - Live driver locations, session cache
- **Spring Security + JWT** - OTP-based phone auth
- **Spring WebSocket (STOMP)** - Real-time location & dispatch
- **Flyway** - Database migrations
- **WebFlux WebClient** - Africa's Talking SMS integration

### Mobile Apps
- **Kotlin + Jetpack Compose** (Android 26+)
- **Google Maps SDK** - Maps & routing
- **Firebase Cloud Messaging** - Push notifications
- **Foreground Services + WorkManager** - Background location tracking

### Admin Dashboard
- **React 18 + TypeScript + Vite**
- **Leaflet / react-leaflet** - Live fleet map
- **Axios** - API client

## Getting Started

### Prerequisites
- Docker & Docker Compose
- JDK 21
- Gradle 8.5+
- Android Studio (for mobile apps)
- Node.js 18+ (for admin dashboard)

### Run with Docker Compose

```bash
# Start PostgreSQL + PostGIS + Redis + Backend
docker compose up --build

# Backend will be available at http://localhost:8080
# API docs: http://localhost:8080/swagger-ui/index.html (if springdoc is added)
```

### Run Backend Locally (without Docker)

```bash
# 1. Start PostgreSQL with PostGIS
docker run -d -p 5432:5432 \
  -e POSTGRES_DB=ridenorth \
  -e POSTGRES_USER=ridenorth \
  -e POSTGRES_PASSWORD=ridenorth \
  postgis/postgis:16-3.3

# 2. Start Redis
docker run -d -p 6379:6379 redis:7-alpine

# 3. Run the backend
cd backend
./gradlew bootRun
```

### Run Mobile Apps

```bash
# Rider App
cd rider-app
# Open in Android Studio and run

# Driver App
cd driver-app
# Open in Android Studio and run
```

### Run Admin Dashboard

```bash
cd admin-dashboard
npm install
npm run dev
# Available at http://localhost:3000
```

## API Endpoints

### Authentication
- `POST /api/auth/request-otp` - Request OTP via SMS
- `POST /api/auth/verify-otp` - Verify OTP and get JWT
- `POST /api/auth/register-driver` - Register as driver
- `GET /api/auth/me` - Get current user profile

### Bookings
- `POST /api/bookings/rides` - Create ride request
- `POST /api/bookings/rides/{id}/accept` - Accept ride (driver)
- `POST /api/bookings/trips/{id}/start` - Start trip
- `POST /api/bookings/trips/{id}/complete` - Complete trip
- `GET /api/bookings/trips` - Get my trips

### Driver
- `POST /api/driver/location` - Update driver location
- `POST /api/driver/status` - Set online/offline
- `GET /api/driver/profile` - Get driver profile
- `GET /api/driver/online` - Get online drivers

### Freight
- `POST /api/freight/jobs` - Create freight job
- `GET /api/freight/jobs` - Get my freight jobs
- `POST /api/freight/jobs/{id}/quotes` - Submit quote (driver)
- `GET /api/freight/jobs/{id}/quotes` - Get quotes for job

### Payments
- `GET /api/payments/wallet` - Get my wallet
- `GET /api/payments/history` - Get payment history
- `POST /api/payments/mtn/initiate` - Initiate MTN MoMo payment

### Location
- `POST /api/location/update` - Update driver location

### USSD
- `POST /api/ussd/handle` - USSD gateway handler

## Database Schema

Core tables: `users`, `vehicles`, `driver_profiles`, `ride_requests`, `trips`, `freight_jobs`, `freight_quotes`, `scheduled_routes`, `payments`, `wallets`, `ratings`, `sos_events`, `ussd_sessions`

All PostGIS geography columns enable efficient radius/nearest-neighbour queries for driver matching.

## MVP Roadmap

- **Phase 0**: Foundation (Backend skeleton, auth, DB schema, CI/CD)
- **Phase 1**: Passenger MVP (Boda + tuk tuk + car on-demand booking, live tracking, MoMo/Airtel payment, ratings)
- **Phase 2**: Trust & Reach (USSD/SMS, SOS, driver verification, cash payment)
- **Phase 3**: Freight Marketplace (Freight jobs, quotes, proof-of-pickup/delivery)
- **Phase 4**: Scheduled Transport (Minibus/coach routes, seat booking)
- **Phase 5**: Expansion (Arua, Kitgum, Nebbi rollout, B2B accounts)

## Environment Variables

```env
JWT_SECRET=your-jwt-secret-key
MOMO_API_KEY=your-mtn-momo-api-key
MOMO_API_SECRET=your-mtn-momo-secret
AIRTEL_API_KEY=your-airtel-api-key
AIRTEL_API_SECRET=your-airtel-secret
AFRICASTALKING_API_KEY=your-africas-talking-key
AFRICASTALKING_USERNAME=your-africas-talking-username
MQTT_USERNAME=
MQTT_PASSWORD=
```

## License

Proprietary - RideNorth (c) 2026
