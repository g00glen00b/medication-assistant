import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CreateAvailabilityRequest} from "../models/create-availability-request";
import {Observable} from "rxjs";
import {Availability} from "../models/availability";
import {PageRequest} from "../../shared/models/page-request";
import {PageInfo} from "../../shared/models/page-info";
import {UpdateAvailabilityRequest} from "../models/update-availability-request";

@Injectable({
  providedIn: 'root'
})
export class AvailabilityService {

  constructor(private httpClient: HttpClient) { }

  create(request: CreateAvailabilityRequest): Observable<Availability> {
    return this.httpClient.post<Availability>(`./api/availability`, request);
  }

  update(id: string, request: UpdateAvailabilityRequest): Observable<Availability> {
    return this.httpClient.put<Availability>(`./api/availability/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`./api/availability/${id}`);
  }

  findAll(request: PageRequest): Observable<PageInfo<Availability>> {
    return this.httpClient.get<PageInfo<Availability>>(`./api/availability?page=${request.page}&size=${request.size}&sort=${request.sort}`);
  }

  findById(id: string): Observable<Availability> {
    return this.httpClient.get<Availability>(`./api/availability/${id}`);
  }
}
