import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Notification} from "../../models/notification";

@Component({
  selector: 'mediminder-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent {
  @Input()
  notifications: Notification[] = [];
  @Output()
  onDismiss: EventEmitter<Notification> = new EventEmitter<Notification>();

  dismiss(notification: Notification): void {
    this.onDismiss.emit(notification);
  }
}
