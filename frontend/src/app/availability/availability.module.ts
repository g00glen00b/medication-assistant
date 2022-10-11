import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AvailabilityPageComponent} from './pages/availability-page/availability-page.component';
import {AvailabilityRoutingModule} from "./availability-routing.module";
import {CreateAvailabilityPageComponent} from './pages/create-availability-page/create-availability-page.component';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {AvailabilityFormComponent} from './components/availability-form/availability-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatCardModule} from "@angular/material/card";
import {AvailabilityCardComponent} from './components/availability-card/availability-card.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {AvailabilityListComponent} from './components/availability-list/availability-list.component';
import {UpdateAvailabilityPageComponent} from './pages/update-availability-page/update-availability-page.component';
import {ScrollingModule} from "@angular/cdk/scrolling";
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {SharedModule} from "../shared/shared.module";


@NgModule({
  declarations: [
    AvailabilityPageComponent,
    CreateAvailabilityPageComponent,
    AvailabilityFormComponent,
    AvailabilityCardComponent,
    AvailabilityListComponent,
    UpdateAvailabilityPageComponent
  ],
  imports: [
    CommonModule,
    AvailabilityRoutingModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatSelectModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatCardModule,
    MatProgressSpinnerModule,
    ScrollingModule,
    InfiniteScrollModule,
    SharedModule
  ]
})
export class AvailabilityModule { }
