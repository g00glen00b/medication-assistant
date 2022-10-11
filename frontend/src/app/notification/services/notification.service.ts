import { Injectable } from '@angular/core';
import {PageRequest} from "../../shared/models/page-request";
import {Observable} from "rxjs";
import {PageInfo} from "../../shared/models/page-info";
import {Notification} from "../models/notification";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private httpClient: HttpClient) { }

  findAll(page: PageRequest): Observable<PageInfo<Notification>> {
    return this.httpClient.get<PageInfo<Notification>>(`./api/notification?page=${page.page}&size=${page.size}&sort=${page.sort}`);
  }

  delete(notification: Notification): Observable<void> {
    return this.httpClient.delete<void>(`./api/notification/${notification.id}`);
  }
}
