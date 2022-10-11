import { Injectable } from '@angular/core';
import {defaultIfEmpty, filter, map, Observable} from "rxjs";
import {ConfirmationInfo} from "../models/confirmation-info";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "../components/confirmation-dialog/confirmation-dialog.component";

@Injectable({
  providedIn: 'root'
})
export class ConfirmationService {

  constructor(private dialog: MatDialog) { }

  confirm(info: ConfirmationInfo): Observable<boolean> {
    return this.dialog
      .open<ConfirmationDialogComponent, ConfirmationInfo, boolean>(ConfirmationDialogComponent, {data: info})
      .afterClosed()
      .pipe(
        filter(response => response != undefined),
        defaultIfEmpty(false),
        map(response => response as boolean));
  }
}
