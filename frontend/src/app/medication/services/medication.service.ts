import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../../shared/models/page-request";
import {Observable} from "rxjs";
import {Medication} from "../models/medication";
import {PageInfo} from "../../shared/models/page-info";
import {QuantityType} from "../models/quantity-type";

@Injectable({
  providedIn: 'root'
})
export class MedicationService {

  constructor(private httpClient: HttpClient) { }

  findAll(search: string, page: PageRequest): Observable<PageInfo<Medication>> {
    return this.httpClient.get<PageInfo<Medication>>(`./api/medication?search=${search}&page=${page.page}&size=${page.size}&sort=${page.sort}`);
  }

  findAllQuantityTypes(page: PageRequest): Observable<PageInfo<QuantityType>> {
    return this.httpClient.get<PageInfo<QuantityType>>(`./api/medication/quantity-type?page=${page.page}&size=${page.size}&sort=${page.sort}`);
  }
}
