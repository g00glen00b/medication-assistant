import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {HttpClientModule} from "@angular/common/http";
import {CentralCardComponent} from './components/central-card/central-card.component';
import {MatCardModule} from "@angular/material/card";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import { NavigationComponent } from './components/navigation/navigation.component';
import {MatListModule} from "@angular/material/list";
import {MatSidenavModule} from "@angular/material/sidenav";
import { ReadableDurationComponent } from './components/readable-duration/readable-duration.component';
import { EmptyStateComponent } from './components/empty-state/empty-state.component';

@NgModule({
  declarations: [
    CentralCardComponent,
    ConfirmationDialogComponent,
    NavigationComponent,
    ReadableDurationComponent,
    EmptyStateComponent,
  ],
    imports: [
        CommonModule,
        HttpClientModule,
        RouterModule,
        MatIconModule,
        MatToolbarModule,
        MatCardModule,
        MatSnackBarModule,
        MatDialogModule,
        MatButtonModule,
        MatListModule,
        MatSidenavModule
    ],
  exports: [
    HttpClientModule,
    CentralCardComponent,
    NavigationComponent,
    ReadableDurationComponent,
    EmptyStateComponent,
  ]
})
export class SharedModule { }