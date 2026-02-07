# Premier League Title Race - Backend

Spring Boot backend service for Premier League title race predictions.

## Features

- **League Table API**: Current standings with position changes
- **Team Form Analysis**: Recent performance metrics
- **Monte Carlo Simulations**: 10,000+ season simulations for title probabilities
- **Run-In Difficulty**: Fixture difficulty scoring based on opponent strength
- **Data Storage**: JPA/Hibernate with H2 (dev) and PostgreSQL (prod)

## API Endpoints

### Table
- `GET /api/table/current` - Current league table
- `GET /api/table/gameweek/{gameweek}` - Historical table by gameweek

### Team
- `GET /api/team/{id}/form` - Team form and recent matches

### Title Race
- `GET /api/title-race/projection` - Current title probability projections
- `POST /api/title-race/simulate?runs=10000` - Run new simulation

### Run-In
- `GET /api/run-in/difficulty` - Fixture difficulty for top teams
- `GET /api/run-in/difficulty/{teamId}` - Team-specific run-in analysis

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+

### Run Locally
```bash
cd backend
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console
Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:pltitlerace`
- Username: `sa`
- Password: (empty)

### Build
```bash
mvn clean package
```

## Configuration

Edit `src/main/resources/application.yml` for:
- Database settings
- Football API configuration
- Server port

For production, set environment variables:
- `DB_HOST`, `DB_PORT`, `DB_NAME`
- `DB_USERNAME`, `DB_PASSWORD`
- `FOOTBALL_API_KEY`

## Tech Stack

- Spring Boot 3.2.1
- Spring Data JPA
- Lombok
- H2 Database (dev)
- PostgreSQL (prod)
- WebClient for external APIs
