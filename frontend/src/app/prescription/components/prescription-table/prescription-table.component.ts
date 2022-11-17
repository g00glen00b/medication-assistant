import {Component, Input} from '@angular/core';
import {Prescription} from "../../models/prescription";
import {ConfirmationService} from "../../../shared/services/confirmation.service";

@Component({
  selector: 'mediminder-prescription-table',
  templateUrl: './prescription-table.component.html',
  styleUrls: ['./prescription-table.component.scss']
})
export class PrescriptionTableComponent {
  @Input()
  prescriptions: Prescription[] = [];
  columns: string[] = ['name', 'doses', 'prescriptions'];

  constructor(private confirmationService: ConfirmationService) {
  }

  showMissingPrescriptionsDialog(prescription: Prescription): void {
    this.confirmationService.confirm({
      message: `
        The number of prescriptions is not available because there is no ${prescription.medication.name} in your inventory. 
        Inventory information is used to determine the initial amount of doses per prescription.
      `,
      cancelText: 'CLOSE',
      showConfirm: false
    });
  }
}
