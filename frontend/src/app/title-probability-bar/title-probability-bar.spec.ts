import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitleProbabilityBar } from './title-probability-bar';

describe('TitleProbabilityBar', () => {
  let component: TitleProbabilityBar;
  let fixture: ComponentFixture<TitleProbabilityBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TitleProbabilityBar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TitleProbabilityBar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
