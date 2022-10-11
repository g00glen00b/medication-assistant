import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Notification} from "../../models/notification";
import {formatDistanceToNow, parseISO} from "date-fns";
import {ThemePalette} from "@angular/material/core";

@Component({
  selector: 'mediminder-notification-card',
  templateUrl: './notification-card.component.html',
  styleUrls: ['./notification-card.component.scss']
})
export class NotificationCardComponent implements OnChanges {
  @Input()
  notification!: Notification;
  @Output()
  onDismiss: EventEmitter<Notification> = new EventEmitter<Notification>();
  formattedDate: string = '';
  color: ThemePalette = 'primary';

  ngOnChanges(changes: SimpleChanges) {
    if (this.notification == null) {
      this.formattedDate = '';
      this.color = 'primary';
    } else {
      this.formattedDate = formatDistanceToNow(parseISO(this.notification.createdDate));
      this.color = this.notification.type == 'INFO' ? 'primary' : 'warn';
    }
  }

  dismiss(): void {
    this.onDismiss.emit(this.notification);
  }
}
