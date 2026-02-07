import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Dashboardpage } from './dashboardpage';

describe('Dashboardpage', () => {
  let component: Dashboardpage;
  let fixture: ComponentFixture<Dashboardpage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dashboardpage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Dashboardpage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
