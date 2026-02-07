# Football-Data.org API Integration

## Overview

The application now supports loading real Premier League data from the [football-data.org API](https://www.football-data.org/) instead of using hardcoded sample data.

## How It Works

On application startup, the `DataInitializer` will:

1. **Check if an API key is configured** via the `FOOTBALL_API_KEY` environment variable
2. **If API key is present**: Load real data from football-data.org
   - Fetch current Premier League standings (all 20 teams)
   - Fetch scheduled fixtures
   - Calculate team strengths based on league position
3. **If API key is missing or API fails**: Fall back to sample data (6 teams, 4 fixtures)

## Getting Your API Key

1. Visit [https://www.football-data.org/client/register](https://www.football-data.org/client/register)
2. Sign up for a free account
3. Copy your API key from the dashboard
4. Add it to your `.env` file:
   ```
   FOOTBALL_API_KEY=your_api_key_here
   ```

## Free Tier Limits

The free tier includes:
- 10 requests per minute
- Access to Premier League data
- Current standings and fixtures
- Team information

## Configuration

### For Local Development

Update `.env`:
```bash
FOOTBALL_API_KEY=your_api_key_here
```

### For Docker Compose

The API key is already configured in `docker-compose.yml`:
```yaml
environment:
  FOOTBALL_API_KEY: ye637cc1cf71949fbaca66c56b24797e4
```

Replace with your own key if needed.

## API Endpoints Used

### 1. Get Standings
```
GET /v4/competitions/PL/standings
```
Returns current Premier League table with:
- Team positions
- Points, wins, draws, losses
- Goals for/against
- Recent form (last 5 games)

### 2. Get Scheduled Matches
```
GET /v4/competitions/PL/matches?status=SCHEDULED
```
Returns upcoming fixtures with:
- Match date and time
- Home and away teams
- Matchday (gameweek) number

## Data Mapping

### Teams
- **External ID**: Team ID from API
- **Name**: Full team name (e.g., "Arsenal FC")
- **Short Name**: Abbreviated name (e.g., "Arsenal")
- **Strength**: Calculated based on league position (1st = 98, 20th = 60)
- **Stats**: Played games, wins, draws, losses, goals, points
- **Form**: Last 5 match results (e.g., "WWDWL")

### Fixtures
- **External ID**: Match ID from API
- **Home/Away Teams**: Linked to Team entities
- **Match Date**: UTC timestamp
- **Gameweek**: Premier League matchday number
- **Status**: SCHEDULED, FINISHED, POSTPONED, or CANCELLED

## Troubleshooting

### No data appears after startup

**Check the logs**:
```bash
docker-compose logs backend
```

**Common issues**:
1. Invalid API key
2. API rate limit exceeded (10 requests/minute on free tier)
3. Network connectivity issues

**Solution**: The app will automatically fall back to sample data if API fails.

### Want to force reload from API

1. Delete the database volume:
   ```bash
   docker-compose down -v
   ```
2. Restart:
   ```bash
   docker-compose up --build
   ```

### Test without API key

Remove or comment out the `FOOTBALL_API_KEY` from `.env` or docker-compose.yml:
```yaml
# FOOTBALL_API_KEY: ye637cc1cf71949fbaca66c56b24797e4
```

The app will use sample data automatically.

## Code Structure

### New Components

1. **`FootballDataClient`** (`client/FootballDataClient.java`)
   - HTTP client for football-data.org API
   - Handles authentication with API key
   - Makes GET requests for standings and matches

2. **`StandingsResponse`** (`client/dto/StandingsResponse.java`)
   - DTO for API standings response
   - Includes nested classes for competition, standings, and team info

3. **`MatchesResponse`** (`client/dto/MatchesResponse.java`)
   - DTO for API matches response
   - Includes nested classes for match details and scores

4. **`FootballDataService`** (`service/FootballDataService.java`)
   - Transforms API data to domain entities
   - Handles saving to database
   - Maps API status codes to internal enums

5. **Updated `DataInitializer`** (`util/DataInitializer.java`)
   - Tries API first if key is configured
   - Falls back to sample data on failure
   - Logs clear messages about data source

## Benefits

- ✅ **Real-time data**: Always up-to-date Premier League information
- ✅ **All 20 teams**: Not limited to top 6 anymore
- ✅ **Actual fixtures**: Real upcoming matches
- ✅ **Automatic fallback**: Works without API key for testing
- ✅ **Easy testing**: Toggle between real and sample data
