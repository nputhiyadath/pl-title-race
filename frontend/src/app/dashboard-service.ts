import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiService } from './api-service';
import { LeagueTableRow, TitleProbability, TeamStanding, TitleRaceProjection, TeamForm, RunInDifficulty } from './models';

@Injectable({ providedIn: 'root' })
export class DashboardService extends ApiService {

  getCurrentTable(): Observable<TeamStanding[]> {
    return this.get<TeamStanding[]>('/table/current');
  }

  getTitleRaceProjection(): Observable<TitleRaceProjection> {
    return this.get<TitleRaceProjection>('/title-race/projection');
  }

  getTeamForm(teamId: number): Observable<TeamForm> {
    return this.get<TeamForm>(`/team/${teamId}/form`);
  }

  getRunInDifficulty(): Observable<RunInDifficulty[]> {
    return this.get<RunInDifficulty[]>('/run-in/difficulty');
  }

  getTitleProbabilities(): Observable<TitleProbability[]> {
    return this.getTitleRaceProjection().pipe(
      map(projection => projection.probabilities.map(p => ({
        team: p.shortName,
        probability: Math.round(p.titleProbability * 100),
        delta: p.probabilityChange ? Math.round(p.probabilityChange * 100) : 0
      })))
    );
  }

  getLeagueTable(): Observable<LeagueTableRow[]> {
    return this.getCurrentTable().pipe(
      map(standings => standings.map(s => ({
        position: s.position,
        team: s.shortName,
        points: s.points,
        goalDifference: s.goalDifference,
        delta: s.positionChange || 0
      })))
    );
  }
}
