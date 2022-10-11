import {Medication} from "../../medication/models/medication";
import {SchedulePeriod} from "./schedule-period";

export interface Schedule {
  id: string;
  medication: Medication;
  quantity: number;
  period: SchedulePeriod;
  interval: string;
  time: string;
  description?: string;
}