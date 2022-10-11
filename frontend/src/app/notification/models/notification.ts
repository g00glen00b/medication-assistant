export interface Notification {
  id: string;
  message: string;
  type: NotificationType;
  createdDate: string;
}

export enum NotificationType {
  INFO = 'INFO',
  WARNING = 'WARNING',
  ERROR = 'ERROR'
}