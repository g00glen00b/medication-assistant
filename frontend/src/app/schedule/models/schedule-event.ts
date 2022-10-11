import {Medication} from "../../medication/models/medication";

export interface ScheduleEvent {
  scheduleId: string;
  medication: Medication;
  quantity: number;
  eventDate: string;
  description?: string;
  completedDate?: string;
}