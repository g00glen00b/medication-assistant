import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationListComponent } from './components/notification-list/notification-list.component';
import {MatCardModule} from "@angular/material/card";
import {MatListModule} from "@angular/material/list";
import { NotificationCardComponent } from './components/notification-card/notification-card.component';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {SharedModule} from "../shared/shared.module";



@NgModule({
  declarations: [
    NotificationListComponent,
    NotificationCardComponent
  ],
  exports: [
    NotificationListComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    SharedModule,
  ]
})
export class NotificationModule { }
