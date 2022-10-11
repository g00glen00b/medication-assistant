import {Component} from '@angular/core';
import {AvailabilityService} from "../../services/availability.service";
import {CreateAvailabilityRequest} from "../../models/create-availability-request";
import {Availability} from "../../models/availability";
import {MessageService} from "../../../shared/services/message.service";
import {Router} from "@angular/router";

@Component({
  selector: 'mediminder-create-availability-page',
  templateUrl: './create-availability-page.component.html',
  styleUrls: ['./create-availability-page.component.scss']
})
export class CreateAvailabilityPageComponent {

  constructor(
      private service: AvailabilityService,
      private messageService: MessageService,
      private router: Router) { }

  create(request: CreateAvailabilityRequest): void {
    const next = (availability: Availability) => {
      this.messageService.showSuccess(`Successfully added ${availability.medication.name} to your inventory`);
      this.router.navigate([`/inventory`]);
    };
    const error = this.messageService.errorHandler(`Adding the medication to your inventory failed due to an unexpected error`);
    this.service.create(request).subscribe({next, error});
  }

  cancel(): void {
    this.router.navigate([`/inventory`]);
  }
}
