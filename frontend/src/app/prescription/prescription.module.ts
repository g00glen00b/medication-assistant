import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PrescriptionPageComponent } from './pages/prescription-page/prescription-page.component';
import {PrescriptionRoutingModule} from "./prescription-routing.module";
import { PrescriptionFormComponent } from './components/prescription-form/prescription-form.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {SharedModule} from "../shared/shared.module";
import {ReactiveFormsModule} from "@angular/forms";
import {MatNativeDateModule} from "@angular/material/core";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import { PrescriptionTableComponent } from './components/prescription-table/prescription-table.component';
import {MatTableModule} from "@angular/material/table";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";



@NgModule({
  declarations: [
    PrescriptionPageComponent,
    PrescriptionFormComponent,
    PrescriptionTableComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    PrescriptionRoutingModule,
    MatFormFieldModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatButtonModule,
    MatInputModule,
    MatTableModule,
    MatCardModule,
    MatIconModule,
  ]
})
export class PrescriptionModule { }
