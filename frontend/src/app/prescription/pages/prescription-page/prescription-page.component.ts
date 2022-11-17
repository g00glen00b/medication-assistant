import {Component, OnDestroy} from '@angular/core';
import {CalculatePrescriptionRequest} from "../../models/calculate-prescription-request";
import {PrescriptionService} from "../../services/prescription.service";
import {Subject, takeUntil} from "rxjs";
import {MessageService} from "../../../shared/services/message.service";
import {Prescription} from "../../models/prescription";

@Component({
  selector: 'mediminder-prescription-page',
  templateUrl: './prescription-page.component.html',
  styleUrls: ['./prescription-page.component.scss']
})
export class PrescriptionPageComponent implements OnDestroy {
  prescriptions: Prescription[] = [];
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private service: PrescriptionService, private messageService: MessageService) { }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  calculatePrescriptions(request: CalculatePrescriptionRequest): void {
    const next = (prescriptions: Prescription[]) => this.prescriptions = prescriptions;
    const error = this.messageService.errorHandler(`Calculating the prescriptions failed due to an unknown problem`);
    this.service
      .calculatePrescriptions(request)
      .pipe(takeUntil(this.componentDestroyed))
      .subscribe({next, error});
  }
}
