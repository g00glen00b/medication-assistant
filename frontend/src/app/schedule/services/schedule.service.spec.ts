import { TestBed } from '@angular/core/testing';

import { ScheduleService } from './schedule.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {HttpClient} from "@angular/common/http";

describe('ScheduleService', () => {
  let service: ScheduleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(HttpClient)]
    });
    service = TestBed.inject(ScheduleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
