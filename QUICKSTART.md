# Quick Start Guide

Get the Premier League Title Race Predictor running in 5 minutes!

## Option 1: Docker (Easiest)

### Prerequisites
- Docker Desktop installed

### Steps
```bash
# 1. Clone and navigate to the project
cd pl-title-race

# 2. Start all services
docker-compose up --build

# 3. Wait for services to start (2-3 minutes)
# You'll see "Started PlTitleRaceApplication" in the logs

# 4. Open your browser
# Frontend: http://localhost:4200
# Backend API: http://localhost:8080/api/table/current
```

That's it! The application is running with sample data.

### Stop the application
```bash
docker-compose down
```

## Option 2: Local Development

### Prerequisites
- Java 17+ (`java -version`)
- Node.js 18+ (`node -v`)
- Maven 3.6+ (`mvn -v`)

### Backend Setup
```bash
# Terminal 1: Start the backend
cd backend
mvn spring-boot:run

# Wait for: "Started PlTitleRaceApplication"
# Backend will run on http://localhost:8080
```

### Frontend Setup
```bash
# Terminal 2: Start the frontend
cd frontend
npm install
npm start

# Wait for: "✔ Compiled successfully"
# Frontend will run on http://localhost:4200
```

## Testing the APIs

### Get League Table
```bash
curl http://localhost:8080/api/table/current
```

### Get Title Probabilities
```bash
curl http://localhost:8080/api/title-race/projection
```

### Run Simulation
```bash
curl -X POST "http://localhost:8080/api/title-race/simulate?runs=1000"
```

## Sample Data

On first run, the application loads sample data:
- 6 Premier League teams (Arsenal, Man City, Liverpool, Spurs, Man Utd, Chelsea)
- Current standings (Gameweek 20)
- Upcoming fixtures
- Team form and statistics

## Next Steps

1. **Explore the Dashboard**
   - View live league table
   - See title probability bars
   - Check team form

2. **Try the Simulator** (if implemented)
   - Navigate to `/simulator`
   - Toggle fixture outcomes
   - See instant probability updates

3. **Connect Real Data**
   - Get an API key from [Football-Data.org](https://www.football-data.org/)
   - Add it to `.env` file: `FOOTBALL_API_KEY=your_key`
   - Implement data sync service

## Troubleshooting

### Backend won't start
- Check if port 8080 is available: `lsof -i :8080`
- Verify Java 17+: `java -version`
- Check logs in terminal

### Frontend won't start
- Check if port 4200 is available: `lsof -i :4200`
- Clear node_modules: `rm -rf node_modules && npm install`
- Check Node version: `node -v` (should be 18+)

### Docker issues
- Restart Docker Desktop
- Clean up: `docker-compose down -v`
- Rebuild: `docker-compose up --build --force-recreate`

### Database connection errors
- Make sure PostgreSQL container is running
- Check environment variables in docker-compose.yml
- View logs: `docker-compose logs backend`

## Development Tips

### Backend Hot Reload
Spring DevTools is enabled - changes to Java files will auto-reload.

### Frontend Hot Reload
Angular CLI watches for changes - UI updates automatically.

### Database Console (H2 - Dev Mode)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:pltitlerace`
- Username: `sa`
- Password: (empty)

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
```

## Need Help?

- Check the main [README.md](README.md)
- Review API documentation
- Open an issue on GitHub
