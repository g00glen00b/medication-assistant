import {Component, Inject} from '@angular/core';
import {ConfirmationInfo} from "../../models/confirmation-info";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'mediminder-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public confirmationInfo: ConfirmationInfo) { }
}
