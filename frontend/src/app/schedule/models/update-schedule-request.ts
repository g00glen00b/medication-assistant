export interface UpdateScheduleRequest {
  quantity: number;
  startingAt: string;
  endingAtInclusive?: string;
  interval: string;
  time: string;
  description?: string;
}