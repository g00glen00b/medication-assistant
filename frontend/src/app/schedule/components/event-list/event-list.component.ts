import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {ScheduleEvent} from "../../models/schedule-event";
import {format} from "date-fns";

@Component({
  selector: 'mediminder-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnChanges {
  @Input()
  events: ScheduleEvent[] = [];
  @Output()
  onComplete: EventEmitter<ScheduleEvent> = new EventEmitter<ScheduleEvent>();
  eventsByTime: Map<string, ScheduleEvent[]> = new Map<string, ScheduleEvent[]>();

  ngOnChanges(changes: SimpleChanges): void {
    this.eventsByTime.clear();
    this.events.forEach(event => {
      const time = format(new Date(event.eventDate), 'HH:mm');
      if (this.eventsByTime.has(time)) {
        this.eventsByTime.set(time, [...this.eventsByTime.get(time)!, event]);
      } else {
        this.eventsByTime.set(time, [event]);
      }
    });
  }

  complete(event: ScheduleEvent): void {
    this.onComplete.next(event);
  }

}
