import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api-service';
import { RunInDifficulty, TitleRaceProjection } from '../models';

interface FixtureSim {
  id: number;
  home: string;
  away: string;
  date: string;
  result?: 'H' | 'D' | 'A';
}

interface TeamProbDisplay {
  team: string;
  probability: number;
}

@Component({
  selector: 'app-simulator-page',
  imports: [CommonModule],
  templateUrl: './simulator-page.html',
  styleUrl: './simulator-page.css',
})
export class SimulatorPage extends ApiService implements OnInit {
  fixtures = signal<FixtureSim[]>([]);
  probabilities = signal<TeamProbDisplay[]>([]);
  loading = signal<boolean>(true);

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading.set(true);

    // Load upcoming fixtures from run-in difficulty
    this.get<RunInDifficulty[]>('/run-in/difficulty').subscribe({
      next: (teams) => {
        const allFixtures = new Map<number, FixtureSim>();

        teams.forEach(team => {
          team.fixtures.forEach(fixture => {
            if (!allFixtures.has(fixture.fixtureId)) {
              allFixtures.set(fixture.fixtureId, {
                id: fixture.fixtureId,
                home: fixture.isHome ? team.teamName : fixture.opponent,
                away: fixture.isHome ? fixture.opponent : team.teamName,
                date: new Date(fixture.matchDate).toLocaleDateString(),
                result: undefined
              });
            }
          });
        });

        // Sort by fixture ID and take first 10
        const fixtureList = Array.from(allFixtures.values())
          .sort((a, b) => a.id - b.id)
          .slice(0, 10);

        this.fixtures.set(fixtureList);
      },
      error: (err) => console.error('Error loading fixtures:', err)
    });

    // Load current title probabilities
    this.get<TitleRaceProjection>('/title-race/projection').subscribe({
      next: (projection) => {
        const probs = projection.probabilities
          .slice(0, 6)
          .map(p => ({
            team: p.shortName,
            probability: Math.round(p.titleProbability * 100)
          }));

        this.probabilities.set(probs);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading probabilities:', err);
        this.loading.set(false);
      }
    });
  }

  selectResult(fixture: FixtureSim, result: 'H' | 'D' | 'A') {
    fixture.result = result;
    // Note: Real simulation would require backend API to process fixture results
    // and rerun Monte Carlo with modified outcomes
  }

  resetFixtures() {
    this.fixtures().forEach(f => f.result = undefined);
  }

  runSimulation() {
    this.loading.set(true);

    // Collect fixtures with selected results
    const constrainedFixtures = this.fixtures()
      .filter(f => f.result !== undefined)
      .map(f => ({
        fixtureId: f.id,
        result: f.result
      }));

    // If no fixtures selected, just reload current projection
    if (constrainedFixtures.length === 0) {
      this.get<TitleRaceProjection>('/title-race/projection').subscribe({
        next: (projection) => {
          this.updateProbabilities(projection);
        },
        error: (err) => {
          console.error('Error loading projection:', err);
          this.loading.set(false);
        }
      });
      return;
    }

    // Send constrained simulation request
    const request = {
      runs: 10000,
      constrainedFixtures: constrainedFixtures
    };

    this.post<TitleRaceProjection>('/title-race/simulate-constrained', request).subscribe({
      next: (projection) => {
        this.updateProbabilities(projection);
      },
      error: (err) => {
        console.error('Error running constrained simulation:', err);
        this.loading.set(false);
      }
    });
  }

  private updateProbabilities(projection: TitleRaceProjection) {
    const probs = projection.probabilities
      .slice(0, 6)
      .map(p => ({
        team: p.shortName,
        probability: Math.round(p.titleProbability * 100)
      }));

    this.probabilities.set(probs);
    this.loading.set(false);
  }
}
