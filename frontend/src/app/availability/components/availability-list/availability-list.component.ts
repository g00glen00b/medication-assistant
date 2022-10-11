import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Availability} from "../../models/availability";

@Component({
  selector: 'mediminder-availability-list',
  templateUrl: './availability-list.component.html',
  styleUrls: ['./availability-list.component.scss']
})
export class AvailabilityListComponent {
  @Input()
  availabilities: Availability[] = [];
  @Output()
  onUpdate: EventEmitter<Availability> = new EventEmitter<Availability>();
  @Output()
  onDelete: EventEmitter<Availability> = new EventEmitter<Availability>();

  update(availability: Availability): void {
    this.onUpdate.emit(availability);
  }

  delete(availability: Availability): void {
    this.onDelete.emit(availability);
  }
}
