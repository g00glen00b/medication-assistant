import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpRequest} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class HttpRequestService {
  private requestsInProgressSubject: BehaviorSubject<HttpRequest<any>[]> = new BehaviorSubject<HttpRequest<any>[]>([]);

  add(request: HttpRequest<any>): void {
    this.requestsInProgressSubject.next([...this.requestsInProgressSubject.value, request]);
  }

  remove(request: HttpRequest<any>): void {
    this.requestsInProgressSubject.next(this.requestsInProgressSubject.value.filter(requestInProgress => requestInProgress !== request));
  }

  observable(): Observable<HttpRequest<any>[]> {
    return this.requestsInProgressSubject.asObservable();
  }
}
