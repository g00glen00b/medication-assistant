import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import {DashboardRoutingModule} from "./dashboard-routing.module";
import {ScheduleModule} from "../schedule/schedule.module";
import {NotificationModule} from "../notification/notification.module";



@NgModule({
  declarations: [
    DashboardPageComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ScheduleModule,
    NotificationModule
  ]
})
export class DashboardModule { }
