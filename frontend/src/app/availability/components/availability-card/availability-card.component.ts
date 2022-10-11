import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Availability} from "../../models/availability";
import {parseISO, subDays} from "date-fns";

@Component({
  selector: 'mediminder-availability-card',
  templateUrl: './availability-card.component.html',
  styleUrls: ['./availability-card.component.scss']
})
export class AvailabilityCardComponent implements OnChanges {
  private thresholdQuantityPercentage: number = 20;
  private thresholdExpiryDays: number = 7;
  @Input()
  availability!: Availability;
  @Output()
  onUpdate: EventEmitter<Availability> = new EventEmitter<Availability>();
  @Output()
  onDelete: EventEmitter<Availability> = new EventEmitter<Availability>();
  progressColor: 'primary' | 'warn' = 'primary';
  quantityPercentage: number = 0;
  expiryDate: Date = new Date();
  showQuantityWarning: boolean = false;
  showExpiryWarning: boolean = false;

  ngOnChanges(changes: SimpleChanges): void {
    this.quantityPercentage = (this.availability.quantity / this.availability.initialQuantity) * 100;
    this.showQuantityWarning = this.quantityPercentage <= this.thresholdQuantityPercentage;
    this.progressColor = this.showQuantityWarning ? 'warn' : 'primary';
    this.expiryDate = parseISO(this.availability.expiryDate);
    this.showExpiryWarning = subDays(this.expiryDate, this.thresholdExpiryDays) <= new Date();
  }

  update(availability: Availability): void {
    this.onUpdate.emit(availability);
  }

  delete(availability: Availability): void {
    this.onDelete.emit(availability);
  }
}
