import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LeagueTableRow } from '../models';

@Component({
  selector: 'app-league-table',
  imports: [CommonModule],
  templateUrl: './league-table.html',
  styleUrl: './league-table.css',
})
export class LeagueTable {
  @Input() rows!: LeagueTableRow[];

}
