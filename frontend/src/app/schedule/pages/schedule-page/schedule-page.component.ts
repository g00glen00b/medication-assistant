import {Component, OnInit} from '@angular/core';
import {ScheduleService} from "../../services/schedule.service";
import {PageRequest} from "../../../shared/models/page-request";
import {BehaviorSubject, filter, mergeMap, Subject, takeUntil} from "rxjs";
import {ConfirmationService} from "../../../shared/services/confirmation.service";
import {MessageService} from "../../../shared/services/message.service";
import {Router} from "@angular/router";
import {ConfirmationInfo} from "../../../shared/models/confirmation-info";
import {Schedule} from "../../models/schedule";

@Component({
  selector: 'mediminder-schedule-page',
  templateUrl: './schedule-page.component.html',
  styleUrls: ['./schedule-page.component.scss']
})
export class SchedulePageComponent implements OnInit {
  allSchedules: Schedule[] = [];
  private pageRequest: PageRequest = PageRequest.firstSortedPage(12, 'time,asc');
  private totalElements: number = 0;
  private pageRequestSubject: BehaviorSubject<PageRequest> = new BehaviorSubject<PageRequest>(this.pageRequest);
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(
    private service: ScheduleService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private router: Router) { }

  ngOnInit(): void {
    this.initializeSchedules();
    this.initializePageRequest();
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  findNextPage(): void {
    const nextPage: PageRequest = this.pageRequest.next();
    const [startIndex] = nextPage.calculateIndexRange();
    if (startIndex <= this.totalElements) {
      this.pageRequestSubject.next(this.pageRequest.next());
    }
  }

  updateSchedule(schedule: Schedule): void {
    this.router.navigate([`/schedule/update/${schedule.id}`]);
  }

  deleteSchedule(schedule: Schedule): void {
    const confirmationInfo: ConfirmationInfo = {message: `Are you sure you want to delete the schedule for ${schedule.medication.name}?`};
    const next = () => {
      this.allSchedules = this.allSchedules.filter(({id}) => id != schedule.id);
      this.messageService.showSuccess(`Medication ${schedule.medication.name} successfully removed from your schedules`);
    };
    const error = this.messageService.errorHandler(`Deleting the ${schedule.medication.name} from your schedules failed due to an unexpected error`);
    this.confirmationService
      .confirm(confirmationInfo)
      .pipe(
        takeUntil(this.componentDestroyed),
        filter(result => result),
        mergeMap(() => this.service.delete(schedule.id)))
      .subscribe({next, error});
  }

  private initializeSchedules() {
    this.pageRequestSubject
      .pipe(
        takeUntil(this.componentDestroyed),
        mergeMap(pageRequest => this.service.findAll(pageRequest)))
      .subscribe(pageInfo => {
        this.totalElements = pageInfo.totalElements;
        this.allSchedules = [...this.allSchedules, ...pageInfo.content];
      });
  }

  private initializePageRequest() {
    this.pageRequestSubject
      .pipe(takeUntil(this.componentDestroyed))
      .subscribe(pageRequest => this.pageRequest = pageRequest);
  }
}
