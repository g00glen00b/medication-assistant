import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../shared/models/page-request";
import {Observable} from "rxjs";
import {Schedule} from "../models/schedule";
import {PageInfo} from "../../shared/models/page-info";
import {CreateScheduleRequest} from "../models/create-schedule-request";
import {UpdateScheduleRequest} from "../models/update-schedule-request";
import {ScheduleEvent} from "../models/schedule-event";
import {format} from "date-fns";

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  constructor(private httpClient: HttpClient) { }

  findAll(page: PageRequest): Observable<PageInfo<Schedule>> {
    return this.httpClient.get<PageInfo<Schedule>>(`./api/schedule?page=${page.page}&size=${page.size}&sort=${page.sort}`);
  }

  findById(id: string): Observable<Schedule> {
    return this.httpClient.get<Schedule>(`./api/schedule/${id}`);
  }

  create(request: CreateScheduleRequest): Observable<Schedule> {
    return this.httpClient.post<Schedule>(`./api/schedule`, request);
  }

  update(id: string, request: UpdateScheduleRequest): Observable<Schedule> {
    return this.httpClient.put<Schedule>(`./api/schedule/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`./api/schedule/${id}`);
  }

  findAllEvents(date: Date): Observable<ScheduleEvent[]> {
    return this.httpClient.get<ScheduleEvent[]>(`./api/schedule/event/${format(date, 'yyyy-MM-dd')}`);
  }

  completeEvent(event: ScheduleEvent): Observable<ScheduleEvent> {
    const date: string = format(new Date(event.eventDate), 'yyyy-MM-dd');
    return this.httpClient.post<ScheduleEvent>(`./api/schedule/${event.scheduleId}/event/${date}`, {});
  }
}
