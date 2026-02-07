import { Routes } from '@angular/router';

import { DashboardPageComponent } from './dashboardpage/dashboardpage';
import { SimulatorPage } from './simulator-page/simulator-page';

export const routes: Routes = [
    {path: '', component: DashboardPageComponent},
    {path: 'simulator', component: SimulatorPage}
];
