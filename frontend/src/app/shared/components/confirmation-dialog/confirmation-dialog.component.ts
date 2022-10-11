import {Component, Inject, OnInit} from '@angular/core';
import {ConfirmationInfo} from "../../models/confirmation-info";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'mediminder-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {
  message: string = '';

  constructor(private dialogRef: MatDialogRef<ConfirmationDialogComponent>, @Inject(MAT_DIALOG_DATA) private confirmationInfo: ConfirmationInfo) { }

  ngOnInit(): void {
    this.message = this.confirmationInfo.message;
  }

}
