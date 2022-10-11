import {Component, OnInit} from '@angular/core';
import {filter, map, mergeMap, Observable, Subject, take, takeUntil} from "rxjs";
import {MessageService} from "../../../shared/services/message.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Schedule} from "../../models/schedule";
import {ScheduleService} from "../../services/schedule.service";
import {CreateScheduleRequest} from "../../models/create-schedule-request";
import {UpdateScheduleRequest} from "../../models/update-schedule-request";

@Component({
  selector: 'mediminder-update-schedule-page',
  templateUrl: './update-schedule-page.component.html',
  styleUrls: ['./update-schedule-page.component.scss']
})
export class UpdateSchedulePageComponent implements OnInit {
  schedule?: Schedule;
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(
    private service: ScheduleService,
    private messageService: MessageService,
    private activatedRoute: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    const next = (result: Schedule) => this.schedule = result;
    const error = this.messageService.errorHandler('This schedule does not exist. You may have deleted it earlier');
    this.activatedRoute.paramMap
      .pipe(
        this.mapParameterId(),
        take(1),
        takeUntil(this.componentDestroyed),
        mergeMap(id => this.service.findById(id)))
      .subscribe({next, error});
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  update(request: CreateScheduleRequest): void {
    const {quantity, time, startingAt, endingAtInclusive, description, interval} = request;
    const updateRequest: UpdateScheduleRequest = {quantity, time, startingAt, endingAtInclusive, description, interval};
    const next = (schedule: Schedule) => {
      this.messageService.showSuccess(`Successfully updated schedule for ${schedule.medication.name}`);
      this.router.navigate([`/schedule`]);
    };
    const error = () => {
      this.messageService.showError(`Updating the schedule for ${this.schedule!.medication.name} failed due to an unexpected error`);
    };
    this.service
      .update(this.schedule!.id, updateRequest)
      .subscribe({next, error});
  }

  cancel(): void {
    this.router.navigate([`/schedule`]);
  }

  private mapParameterId() {
    return (observable: Observable<ParamMap>) => observable.pipe(
      map(parameters => parameters.get('id')),
      filter(id => id != null),
      map(id => id as string)
    );
  }
}
