# Event Booking System

A comprehensive event management and ticket booking platform built with Spring Boot, featuring OAuth2 authentication via Keycloak, PostgreSQL database, and QR code generation for tickets.

## Features

### 🎫 Event Management
- Create, update, and delete events (organizers only)
- Multiple ticket types per event with pricing and availability
- Event status management (DRAFT, PUBLISHED, CANCELLED, COMPLETED)
- Full-text search for published events

### 🎟️ Ticket Management
- Purchase tickets for published events
- QR code generation for purchased tickets
- Ticket validation system (QR scan and manual)
- User ticket history and details

### 🔐 Security & Authentication
- OAuth2 Resource Server integration with Keycloak
- Role-based access control (ORGANIZER, STAFF roles)
- JWT token-based authentication
- Automatic user provisioning from Keycloak

### 🔍 Search & Discovery
- PostgreSQL full-text search for events
- Event filtering by status and organizer
- Paginated API responses

## Technology Stack

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 23
- **Database**: PostgreSQL
- **Authentication**: OAuth2 + Keycloak
- **ORM**: Spring Data JPA with Hibernate
- **QR Code**: Google ZXing library
- **Mapping**: MapStruct
- **Build Tool**: Maven
- **Containerization**: Docker Compose

## Prerequisites

- Java 23 or higher
- Maven 3.9+
- Docker and Docker Compose
- Git

## Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Event-Booking
```

### 2. Start Infrastructure Services
```bash
docker-compose up -d
```

This will start:
- PostgreSQL database (port 5432)
- Keycloak authentication server (port 9090)
- Adminer database management (port 8888)

### 3. Configure Keycloak

1. Access Keycloak at http://localhost:9090
2. Login with admin/admin
3. Create a realm named `event-ticket-platform`
4. Create roles: `ROLE_ORGANIZER`, `ROLE_STAFF`
5. Create users and assign appropriate roles

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080

## API Documentation

### Authentication
All API endpoints (except public event listings) require a valid JWT token from Keycloak.

Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

### Event Management APIs

#### Create Event (Organizer Only)
```http
POST /api/v1/events
Content-Type: application/json

{
  "name": "Tech Conference 2024",
  "start": "2024-12-01T10:00:00",
  "end": "2024-12-01T18:00:00",
  "venue": "Convention Center",
  "salesStart": "2024-11-01T00:00:00",
  "salesEnd": "2024-11-30T23:59:59",
  "status": "PUBLISHED",
  "ticketTypes": [
    {
      "name": "Early Bird",
      "price": 99.99,
      "ticketsAvailable": 100,
      "description": "Early bird discount"
    }
  ]
}
```

#### List Events for Organizer
```http
GET /api/v1/events?page=0&size=10
```

#### Get Event Details
```http
GET /api/v1/events/{eventId}
```

#### Update Event
```http
PUT /api/v1/events/{eventId}
```

#### Delete Event
```http
DELETE /api/v1/events/{eventId}
```

### Public Event APIs

#### List Published Events
```http
GET /api/v1/published-events?page=0&size=10
```

#### Search Events
```http
GET /api/v1/published-events?q=conference&page=0&size=10
```

#### Get Published Event Details
```http
GET /api/v1/published-events/{eventId}
```

### Ticket Management APIs

#### Purchase Ticket
```http
POST /api/v1/events/{eventId}/ticket-types/{ticketTypeId}/tickets
```

#### List User Tickets
```http
GET /api/v1/tickets?page=0&size=10
```

#### Get Ticket Details
```http
GET /api/v1/tickets/{ticketId}
```

#### Get Ticket QR Code
```http
GET /api/v1/tickets/{ticketId}/qr-codes
```

### Ticket Validation APIs (Staff Only)

#### Validate Ticket
```http
POST /api/v1/ticket-validations
Content-Type: application/json

{
  "id": "ticket-or-qrcode-uuid",
  "method": "QR_SCAN" // or "MANUAL"
}
```

## Database Schema

### Key Entities
- **Users**: User information from Keycloak
- **Events**: Event details with organizer relationship
- **TicketTypes**: Different ticket categories per event
- **Tickets**: Individual ticket purchases
- **QrCodes**: QR codes for ticket validation
- **TicketValidations**: Validation history

### Relationships
- User → Events (One-to-Many, as organizer)
- Event → TicketTypes (One-to-Many)
- TicketType → Tickets (One-to-Many)
- Ticket → QrCodes (One-to-Many)
- Ticket → TicketValidations (One-to-Many)

## Configuration

### Application Properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=Sameer@48

# OAuth2
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/event-ticket-platform
```

### Docker Compose Services
- **PostgreSQL**: Database server
- **Keycloak**: Authentication and authorization
- **Adminer**: Database administration interface

## Development

### Running Tests
```bash
./mvnw test
```

### Building the Application
```bash
./mvnw clean package
```

### Code Quality
The project uses:
- Lombok for reducing boilerplate code
- MapStruct for object mapping
- Spring Boot DevTools for development
- Validation annotations for input validation

## Security Features

### Role-based Access Control
- **ROLE_ORGANIZER**: Can create, manage, and delete events
- **ROLE_STAFF**: Can validate tickets
- **Authenticated Users**: Can purchase tickets and view their purchases

### Concurrency Control
- Pessimistic locking for ticket purchases to prevent overselling
- Transactional operations for data consistency

## Error Handling

Global exception handling provides consistent error responses:
- `EventNotFoundException`: 400 Bad Request
- `TicketSoldOutException`: 400 Bad Request  
- `QrCodeGenerationException`: 500 Internal Server Error
- Validation errors: 400 Bad Request with field details

## Monitoring and Logging

- Structured logging with SLF4J
- JPA query logging (configurable)
- Request/response logging for debugging

## Deployment

### Docker Deployment
```bash
# Build the application
./mvnw clean package

# Build Docker image
docker build -t event-booking .

# Run with Docker Compose
docker-compose up
```

### Environment Variables
- `DATABASE_URL`: PostgreSQL connection string
- `KEYCLOAK_ISSUER_URI`: Keycloak realm issuer URI
- `SPRING_PROFILES_ACTIVE`: Active Spring profile

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the API examples above

---

**Built with ❤️ using Spring Boot and modern Java technologies**
