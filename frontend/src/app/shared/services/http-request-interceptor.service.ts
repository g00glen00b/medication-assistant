import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {HttpRequestService} from "./http-request.service";
import {finalize, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HttpRequestInterceptorService implements HttpInterceptor {
  constructor(private httpRequestService: HttpRequestService) {
  }

  intercept(httpRequest: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.httpRequestService.add(httpRequest);
    return next
      .handle(httpRequest)
      .pipe(finalize(() => this.httpRequestService.remove(httpRequest)));
  }
}
