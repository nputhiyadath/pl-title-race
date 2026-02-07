import { Component, OnInit, signal } from '@angular/core';
import { DashboardService } from '../dashboard-service';
import { LeagueTableRow, TitleProbability } from '../models';
import { LeagueTable } from '../league-table/league-table';
import { TitleProbabilityBar } from '../title-probability-bar/title-probability-bar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard-page',
  imports: [LeagueTable, TitleProbabilityBar, CommonModule],
  templateUrl: './dashboardpage.html',
  standalone: true
})
export class DashboardPageComponent implements OnInit {

  probabilities = signal<TitleProbability[]>([]);
  table = signal<LeagueTableRow[]>([]);

  constructor(
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    console.log('Dashboard component initialized');

    this.dashboardService.getTitleProbabilities()
      .subscribe({
        next: (data) => {
          console.log('Received probabilities:', data);
          this.probabilities.set(data);
          console.log('Signal probabilities set, current value:', this.probabilities());
        },
        error: (error) => console.error('Error loading probabilities:', error)
      });

    this.dashboardService.getLeagueTable()
      .subscribe({
        next: (data) => {
          console.log('Received table data:', data);
          this.table.set(data);
          console.log('Signal table set, current value:', this.table());
        },
        error: (error) => console.error('Error loading table:', error)
      });
  }
}