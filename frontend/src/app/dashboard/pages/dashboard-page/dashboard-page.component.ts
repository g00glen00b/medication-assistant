import {Component, OnDestroy, OnInit} from '@angular/core';
import {BehaviorSubject, map, mergeMap, Subject, takeUntil} from "rxjs";
import {ScheduleService} from "../../../schedule/services/schedule.service";
import {ScheduleEvent} from "../../../schedule/models/schedule-event";
import {MessageService} from "../../../shared/services/message.service";
import {User} from "../../../authentication/models/user";
import {AuthenticationService} from "../../../authentication/services/authentication.service";
import {NotificationService} from "../../../notification/services/notification.service";
import {PageRequest} from "../../../shared/models/page-request";
import {Notification} from "../../../notification/models/notification";

const notificationSort = PageRequest.firstSortedPage(10, 'createdDate,asc');

@Component({
  selector: 'mediminder-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent implements OnInit, OnDestroy {
  events: ScheduleEvent[] = [];
  notifications: Notification[] = [];
  user?: User;
  private dateSubject: BehaviorSubject<Date> = new BehaviorSubject<Date>(new Date());
  private notificationPageRequestSubject: BehaviorSubject<PageRequest> = new BehaviorSubject<PageRequest>(notificationSort);
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(
    private scheduleService: ScheduleService,
    private notificationService: NotificationService,
    private authenticationService: AuthenticationService,
    private messageService: MessageService) { }

  ngOnInit(): void {
    this.initializeEvents();
    this.initializeNotifications();
    this.initializeUser();
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  completeEvent(event: ScheduleEvent): void {
    const next = (event: ScheduleEvent) => {
      this.messageService.showSuccess(`Well done!`);
      this.events = this.events.map(currentEvent => {
        return currentEvent.scheduleId == event.scheduleId ? event : currentEvent;
      });
    };
    const error = this.messageService.errorHandler(`The status of the event could not be updated due to an unexpected problem`);
    this.scheduleService
      .completeEvent(event)
      .subscribe({next, error});
  }

  dismissNotification(notification: Notification): void {
    const next = () => {
      this.messageService.showSuccess(`Notification dismissed`);
      this.notificationPageRequestSubject.next(notificationSort)
    };
    const error = this.messageService.errorHandler(`The notification could not be dismissed due to an unexpected problem`);
    this.notificationService
      .delete(notification)
      .subscribe({next, error});
  }

  private initializeUser() {
    this.authenticationService
      .getCurrentUser()
      .pipe(
        takeUntil(this.componentDestroyed))
      .subscribe(user => this.user = user);
  }

  private initializeNotifications() {
    this.notificationPageRequestSubject
      .pipe(
        takeUntil(this.componentDestroyed),
        mergeMap(pageRequest => this.notificationService.findAll(pageRequest)),
        map(page => page.content))
      .subscribe(notifications => this.notifications = notifications);
  }

  private initializeEvents() {
    this.dateSubject
      .pipe(
        takeUntil(this.componentDestroyed),
        mergeMap(date => this.scheduleService.findAllEvents(date)))
      .subscribe(events => this.events = events);
  }
}
