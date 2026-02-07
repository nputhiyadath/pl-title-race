# Premier League Title Race Predictor

A live Premier League title-race intelligence platform that provides real-time predictions, simulations, and analysis of the championship race.

## Features

### Core Features (Phase 1)
- **Live League Table**: Current standings with position changes and deltas
- **Title Probability Engine**: Monte Carlo simulations (10,000+ runs) to calculate title chances
- **Team Form Analysis**: Recent performance metrics and match history
- **Run-In Difficulty**: Weighted fixture difficulty scores based on opponent strength
- **Interactive Dashboard**: Real-time visualization of the title race

### Advanced Features (Phase 2 - Planned)
- **Fixture Simulator**: "What if" scenarios for remaining fixtures
- **AI Matchweek Summaries**: Natural language narratives of key changes
- **Historical Tracking**: Week-by-week probability changes
- **API Access**: Public API for bloggers and analysts

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.1 (Java 17)
- **Database**: PostgreSQL (production), H2 (development)
- **ORM**: Spring Data JPA / Hibernate
- **API**: RESTful with CORS support

### Frontend
- **Framework**: Angular 21
- **Styling**: Tailwind CSS
- **Charts**: D3.js / Recharts (planned)
- **State**: RxJS Observables

### DevOps
- **Containerization**: Docker & Docker Compose
- **CI/CD**: GitHub Actions (planned)
- **Deployment**: AWS/GCP ready

## Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.6+
- Docker & Docker Compose (optional)

### Run with Docker (Recommended)

```bash
# Start all services (PostgreSQL, Backend, Frontend)
docker-compose up --build

# Access the application
# Frontend: http://localhost:4200
# Backend API: http://localhost:8080/api
# PostgreSQL: localhost:5432
```

### Run Locally (Development)

#### Backend
```bash
cd backend
mvn spring-boot:run

# H2 Console: http://localhost:8080/h2-console
```

#### Frontend
```bash
cd frontend
npm install
npm start

# Frontend: http://localhost:4200
```

## API Endpoints

### Table
- `GET /api/table/current` - Current league standings
- `GET /api/table/gameweek/{gw}` - Historical table snapshot

### Team
- `GET /api/team/{id}/form` - Team form and recent matches

### Title Race
- `GET /api/title-race/projection` - Current title probabilities
- `POST /api/title-race/simulate?runs=10000` - Run new simulation

### Run-In
- `GET /api/run-in/difficulty` - Fixture difficulty for top teams
- `GET /api/run-in/difficulty/{teamId}` - Team-specific analysis

## Architecture

```
┌─────────────┐      HTTP/REST      ┌──────────────┐
│   Angular   │◄───────────────────►│ Spring Boot  │
│  Frontend   │      API Calls      │   Backend    │
└─────────────┘                     └──────────────┘
                                           │
                                           ▼
                                    ┌──────────────┐
                                    │  PostgreSQL  │
                                    │   Database   │
                                    └──────────────┘
```

### Key Components

#### Backend Services
- **TableService**: League table management
- **TeamService**: Team statistics and form
- **SimulationService**: Monte Carlo engine
- **RunInService**: Fixture difficulty calculation

#### Frontend Components
- **DashboardPage**: Main view with table and probabilities
- **LeagueTable**: Sortable standings component
- **TitleProbabilityBar**: Visual probability display
- **SimulatorPage**: Interactive fixture simulation

## Simulation Algorithm

The Monte Carlo simulation engine:

1. **Team Strength Calculation**
   ```
   P(win) = baseStrength + homeAdvantage + formFactor
   ```

2. **10,000 Season Simulations**
   - Each match outcome weighted by team strength, location, and recent form
   - Win: 3 points, Draw: 1 point, Loss: 0 points

3. **Probability Output**
   - Title probability: % of simulations where team finishes 1st
   - Top 4 probability: % of simulations in Champions League spots
   - Average/Min/Max final points projections

## Configuration

### Backend Configuration
Edit [backend/src/main/resources/application.yml](backend/src/main/resources/application.yml):
- Database settings
- Server port
- Football API credentials

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=pltitlerace
DB_USERNAME=pluser
DB_PASSWORD=plpassword

# External API
FOOTBALL_API_KEY=your_api_key_here
```

### Frontend Configuration
Edit [frontend/src/app/api-service.ts](frontend/src/app/api-service.ts):
```typescript
private readonly BASE_URL = 'http://localhost:8080/api';
```

## Data Sources

The application automatically loads real Premier League data from the **football-data.org API** on startup.

### API Integration
- **Primary Source**: [Football-Data.org](https://www.football-data.org/) (free tier included)
- **Automatic Loading**: Standings and fixtures loaded on app startup
- **Fallback**: Sample data (6 teams) if API key not configured
- **Get API Key**: Sign up at [football-data.org/client/register](https://www.football-data.org/client/register)

For detailed API integration instructions, see [API_INTEGRATION.md](API_INTEGRATION.md).

### What Gets Loaded
✅ All 20 Premier League teams with current standings
✅ Live stats: points, wins, draws, losses, goals, form
✅ Scheduled upcoming fixtures with dates and gameweeks
✅ Team strengths calculated from league position

## Project Structure

```
pl-title-race/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/pltitlerace/
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── model/       # JPA entities
│   │       ├── repository/  # Data access
│   │       └── dto/         # API response objects
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/                # Angular frontend
│   ├── src/app/
│   │   ├── dashboardpage/   # Main dashboard
│   │   ├── simulator-page/  # Fixture simulator
│   │   ├── league-table/    # Table component
│   │   └── title-probability-bar/
│   ├── package.json
│   └── Dockerfile
│
├── docker-compose.yml       # Multi-container setup
└── README.md
```

## Development Roadmap

### Phase 1 (Current)
- [x] Backend API architecture
- [x] Database models
- [x] Monte Carlo simulation engine
- [x] Basic Angular UI
- [x] Docker setup

### Phase 2 (Next)
- [x] External API integration (Football-Data.org) ✅
- [ ] Scheduled data updates (automatic refresh)
- [ ] Advanced charts (D3.js)
- [ ] Responsive mobile design
- [ ] AI narrative summaries

### Phase 3 (Future)
- [ ] User accounts and preferences
- [ ] Historical season analysis
- [ ] Multi-league support
- [ ] Public API with rate limiting
- [ ] Premium features

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/my-feature`
5. Submit a Pull Request

## License

MIT License - feel free to use this project for learning or building your own football analytics platform.

## Acknowledgments

- Premier League for the inspiration
- Football-Data.org for match data
- Spring Boot and Angular communities
