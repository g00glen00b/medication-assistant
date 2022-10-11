import { TestBed } from '@angular/core/testing';

import { ConfirmationService } from './confirmation.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {MatDialog} from "@angular/material/dialog";

describe('ConfirmationService', () => {
  let service: ConfirmationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(MatDialog)]
    });
    service = TestBed.inject(ConfirmationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
