import { TestBed } from '@angular/core/testing';

import { MessageService } from './message.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {MatSnackBar} from "@angular/material/snack-bar";

describe('MessageService', () => {
  let service: MessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(MatSnackBar)]
    });
    service = TestBed.inject(MessageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
