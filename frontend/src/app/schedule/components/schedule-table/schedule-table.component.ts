import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Schedule} from "../../models/schedule";

@Component({
  selector: 'mediminder-schedule-table',
  templateUrl: './schedule-table.component.html',
  styleUrls: ['./schedule-table.component.scss']
})
export class ScheduleTableComponent {
  @Input()
  schedules: Schedule[] = [];
  @Output()
  onDelete: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  @Output()
  onUpdate: EventEmitter<Schedule> = new EventEmitter<Schedule>();
  columns: string[] = ['name', 'description', 'when', 'actions'];

  delete(schedule: Schedule): void {
    this.onDelete.next(schedule);
  }

  update(schedule: Schedule): void {
    this.onUpdate.next(schedule);
  }
}
