import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'inventory',
    loadChildren: () => import(`./availability/availability.module`).then(module => module.AvailabilityModule)
  },
  {
    path: 'schedule',
    loadChildren: () => import(`./schedule/schedule.module`).then(module => module.ScheduleModule)
  },
  {
    path: 'dashboard',
    loadChildren: () => import(`./dashboard/dashboard.module`).then(module => module.DashboardModule)
  },
  {
    path: 'authentication',
    loadChildren: () => import(`./authentication/authentication.module`).then(module => module.AuthenticationModule)
  },
  {
    path: 'prescription',
    loadChildren: () => import(`./prescription/prescription.module`).then(module => module.PrescriptionModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
