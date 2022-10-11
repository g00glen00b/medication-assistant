import { TestBed } from '@angular/core/testing';

import { MedicationService } from './medication.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {HttpClient} from "@angular/common/http";

describe('MedicationService', () => {
  let service: MedicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(HttpClient)]
    });
    service = TestBed.inject(MedicationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
