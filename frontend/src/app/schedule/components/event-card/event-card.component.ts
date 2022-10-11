import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ScheduleEvent} from "../../models/schedule-event";

@Component({
  selector: 'mediminder-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss']
})
export class EventCardComponent {
  @Input()
  event!: ScheduleEvent;
  @Output()
  onComplete: EventEmitter<ScheduleEvent> = new EventEmitter<ScheduleEvent>();

  complete(): void {
    this.onComplete.next(this.event);
  }
}
