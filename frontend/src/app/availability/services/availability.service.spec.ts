import { TestBed } from '@angular/core/testing';

import { AvailabilityService } from './availability.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {HttpClient} from "@angular/common/http";

describe('AvailabilityService', () => {
  let service: AvailabilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(HttpClient)]
    });
    service = TestBed.inject(AvailabilityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
