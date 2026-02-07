import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TitleProbability } from '../models';

@Component({
  selector: 'app-title-probability-bar',
  imports: [CommonModule],
  templateUrl: './title-probability-bar.html',
  styleUrl: './title-probability-bar.css',
})
export class TitleProbabilityBar {

  @Input() data!: TitleProbability[];

}