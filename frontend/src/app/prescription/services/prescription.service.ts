import { Injectable } from '@angular/core';
import {CalculatePrescriptionRequest} from "../models/calculate-prescription-request";
import {Observable} from "rxjs";
import {Prescription} from "../models/prescription";
import {HttpClient} from "@angular/common/http";
import {format} from "date-fns";

@Injectable({
  providedIn: 'root'
})
export class PrescriptionService {

  constructor(private httpClient: HttpClient) { }

  calculatePrescriptions(request: CalculatePrescriptionRequest): Observable<Prescription[]> {
    return this.httpClient.get<Prescription[]>(`./api/prescription/${format(request.date, 'yyyy-MM-dd')}`);
  }
}
