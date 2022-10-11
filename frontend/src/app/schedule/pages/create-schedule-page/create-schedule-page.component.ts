import {Component} from '@angular/core';
import {ScheduleService} from "../../services/schedule.service";
import {MessageService} from "../../../shared/services/message.service";
import {Router} from "@angular/router";
import {CreateScheduleRequest} from "../../models/create-schedule-request";

@Component({
  selector: 'mediminder-create-schedule-page',
  templateUrl: './create-schedule-page.component.html',
  styleUrls: ['./create-schedule-page.component.scss']
})
export class CreateSchedulePageComponent {

  constructor(
    private service: ScheduleService,
    private messageService: MessageService,
    private router: Router) { }

  create(request: CreateScheduleRequest): void {
    const next = () => {
      this.messageService.showSuccess(`The schedule was successfully created`);
      this.router.navigate([`/schedule`]);
    };
    const error = this.messageService.errorHandler(`The schedule could not be created due to an unexpected error`);
    this.service.create(request).subscribe({next, error});
  }

  cancel(): void {
    this.router.navigate([`/schedule`]);
  }
}
