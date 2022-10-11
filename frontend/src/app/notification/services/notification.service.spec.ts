import { TestBed } from '@angular/core/testing';

import { NotificationService } from './notification.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {HttpClient} from "@angular/common/http";

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(HttpClient)]
    });
    service = TestBed.inject(NotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
