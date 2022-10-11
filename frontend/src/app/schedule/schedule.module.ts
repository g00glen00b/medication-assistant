import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SchedulePageComponent } from './pages/schedule-page/schedule-page.component';
import {ScheduleRoutingModule} from "./schedule-routing.module";
import { CreateSchedulePageComponent } from './pages/create-schedule-page/create-schedule-page.component';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { ScheduleFormComponent } from './components/schedule-form/schedule-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import { ScheduleTableComponent } from './components/schedule-table/schedule-table.component';
import {MatTableModule} from "@angular/material/table";
import {SharedModule} from "../shared/shared.module";
import {MatCardModule} from "@angular/material/card";
import { UpdateSchedulePageComponent } from './pages/update-schedule-page/update-schedule-page.component';
import { EventListComponent } from './components/event-list/event-list.component';
import { EventCardComponent } from './components/event-card/event-card.component';
import {MatCheckboxModule} from "@angular/material/checkbox";



@NgModule({
  declarations: [
    SchedulePageComponent,
    CreateSchedulePageComponent,
    ScheduleFormComponent,
    ScheduleTableComponent,
    UpdateSchedulePageComponent,
    EventListComponent,
    EventCardComponent
  ],
  exports: [
    EventCardComponent,
    EventListComponent
  ],
  imports: [
    CommonModule,
    ScheduleRoutingModule,
    MatIconModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    SharedModule,
    MatCardModule,
    MatCheckboxModule,
  ]
})
export class ScheduleModule { }
