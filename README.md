# CargoFlow

CargoFlow is a full-stack freight transportation management application designed to help logistics companies manage trips, drivers and vehicles.

The system provides role-based functionality for administrators, managers, dispatchers and drivers, including trip management, fleet management, real-time driver tracking and AI-assisted driver and vehicle assignment.

The application was developed as part of my Master's thesis in Software Engineering at the University of Bucharest.

## Key Features
- User authentication and role-based access
- Driver and dispatcher management
- Vehicle and fleet management
- Trip creation and management
- Trip filtering by status, location and date
- Driver and vehicle assignment
- AI-assisted trip assignment
- Real-time driver location tracking using WebSockets
- Operational dashboard with trip and vehicle statistics

## Demo
### Login
<img width="300" alt="LoginMenu" src="https://github.com/user-attachments/assets/244e9f84-5d65-4f37-b323-f36aa5cca142" />

### Admin/Dispatcher dashboard
<img width="300" alt="HomeMenuAdmin" src="https://github.com/user-attachments/assets/b182a074-ec12-4e6d-b4e5-eda1e8e65ba8" />

### Driver dashboard
<img width="300" alt="HomeMenuDrivers" src="https://github.com/user-attachments/assets/15908ef1-0a91-400b-b299-cc31ba7293a2" />

### Live driver tracking
<img width="300" alt="LiveDriverTracking" src="https://github.com/user-attachments/assets/9b6b557b-30c1-4c41-8747-ed38f48b88b3" />

### AI-Assisted driver and vehicle recommendation
https://github.com/user-attachments/assets/b7ab0774-ef93-4936-90d7-415a5a30df30


## Architecture
CargoFlow uses a microservices-based architecture.

Main components:
- **backendCargoFlow** – core business logic and REST APIs
- **assignment-ai** – AI-assisted driver and vehicle assignment
- **location-service** – real-time driver location management
- **api-gateway** – entry point for backend services
- **discovery-service** – service discovery using Eureka
- **officeApp** – Android client application

## Tech Stack
### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- REST APIs
- WebSocket
- OpenAPI / Swagger
- Eureka Service Discovery
- Spring Cloud Gateway
- Maven

### Android
- Kotlin
- Jetpack Compose
- Hilt
- Retrofit
- Kotlin Coroutines
- Kotlin Flow

### AI / Machine Learning
- Random Forest classifier
- AI-assisted driver and vehicle assignment

## Main Flows
### Trip Assignment

1. Dispatcher creates a new trip.
2. The trip is initially marked as `PLANNED`.
3. Available drivers and vehicles are retrieved.
4. The dispatcher can manually select a driver and vehicle or request an AI recommendation.
5. The selected resources are assigned to the trip.
6. The trip status changes to `ASSIGNED`.

### Live Driver Tracking

Driver locations are transmitted to the backend and displayed in real time on the dispatcher map using WebSocket communication.

## Requirements
To run CargoFlow locally, you will need:
- Java 21
- PostgreSQL (tested with 18.0)
- Android Studio
- Android SDK (compileSdk 36, minSdk 26)
- Google Maps API key
- Git

Maven does not need to be installed separately, as the backend services include the Maven Wrapper.

## Running the project
**1. Clone the repository**

**2. Configure PostgreSQL**

Create the PostgreSQL databases required by the backend services and update the database URL, username and password in their configuration files.

The following services use PostgreSQL:
- backendCargoFlow
- location-service
- assignment-ai

Flyway will automatically run the database migrations when the services start.

**3. Configure the backend services**

Each service contains an example configuration file. Create an application.yaml file from the provided example and replace the placeholder values with your local configuration.

Configure:
- PostgreSQL connection details
- Eureka service URL
- JWT secret
- Server address where required
- Google Maps API key for the AI assignment service

The same JWT secret should be used by the services that validate authentication tokens.

**4. Start the Discovery Service**

The Eureka dashboard will be available at: **http://localhost:8761**

**5. Start the backend services**

Open a separate terminal for each service and start:
- backendCargoFlow
- location-service
- assignment-ai
- api-gateway

The default service ports are:

8080 -> backendCargoFlow

8081 -> api-gateway

8082 -> location-service

8083 -> assignment-ai

**6. Configure and run the Android application**

Open the officeApp project in Android Studio.

Configure your Google Maps API key using the provided local properties example and make sure the application points to the correct API Gateway address.

If the application is running on a physical Android device, the backend machine and phone should be accessible on the same network and the backend should be reached using the machine's local network IP instead of **localhost**.

Build and run the application from Android Studio.
