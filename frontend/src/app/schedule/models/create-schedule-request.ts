export interface CreateScheduleRequest {
  medicationId: string;
  quantity: number;
  startingAt: string;
  endingAtInclusive?: string;
  interval: string;
  time: string;
  description?: string;
}