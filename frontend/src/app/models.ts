export interface TeamStanding {
  id: number;
  name: string;
  shortName: string;
  crestUrl?: string;
  position: number;
  previousPosition?: number;
  positionChange?: number;
  playedGames: number;
  won: number;
  draw: number;
  lost: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDifference: number;
  points: number;
  form?: string;
}

export interface TeamProbability {
  teamId: number;
  teamName: string;
  shortName: string;
  titleProbability: number;
  top4Probability?: number;
  previousTitleProbability?: number;
  probabilityChange?: number;
  currentPoints: number;
  averageFinalPoints?: number;
  minFinalPoints?: number;
  maxFinalPoints?: number;
}

export interface TitleRaceProjection {
  currentGameweek: number;
  probabilities: TeamProbability[];
  simulationRuns: number;
  narrative?: string;
}

export interface TeamForm {
  teamId: number;
  teamName: string;
  form?: string;
  recentMatches: MatchSummary[];
  pointsLastFive: number;
  averageGoalsScored: number;
  averageGoalsConceded: number;
}

export interface MatchSummary {
  fixtureId: number;
  homeTeam: string;
  awayTeam: string;
  homeScore: number;
  awayScore: number;
  matchDate: string;
  result: string;
}

export interface RunInDifficulty {
  teamId: number;
  teamName: string;
  difficultyScore: number;
  remainingFixtures: number;
  fixtures: FixtureDifficulty[];
}

export interface FixtureDifficulty {
  fixtureId: number;
  opponent: string;
  isHome: boolean;
  matchDate: string;
  difficulty: number;
  opponentPosition: number;
}

export interface TitleProbability {
  team: string;
  probability: number;
  delta: number;
}

export interface LeagueTableRow {
  position: number;
  team: string;
  points: number;
  goalDifference: number;
  delta: number;
}

export interface FixtureSimulation {
  home: string;
  away: string;
  result?: 'H' | 'D' | 'A';
}

export interface SimulationResult {
  team: string;
  probability: number;
  delta: number;
}
