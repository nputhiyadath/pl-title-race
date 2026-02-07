import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ApiService } from './api-service';
import { FixtureSimulation, SimulationResult } from './models';

@Injectable({ providedIn: 'root' })
export class SimulationService extends ApiService {

  simulate(fixtures: FixtureSimulation[]): Observable<SimulationResult[]> {
    // TEMP mock logic
    const wins = fixtures.filter(f => f.result === 'H').length;

    return of([
      { team: 'Arsenal', probability: 47 + wins, delta: +wins },
      { team: 'Man City', probability: 43 - wins, delta: -wins },
      { team: 'Liverpool', probability: 10, delta: 0 }
    ]);
  }
}
