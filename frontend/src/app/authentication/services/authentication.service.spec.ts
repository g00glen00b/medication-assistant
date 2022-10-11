import { TestBed } from '@angular/core/testing';

import { AuthenticationService } from './authentication.service';
import {provideMock} from "../../../../test/jasmine-utils";
import {HttpClient} from "@angular/common/http";
import {of} from "rxjs";
import {User} from "../models/user";

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpMock;

  beforeEach(() => {
    httpMock = provideMock(HttpClient);
    TestBed.configureTestingModule({
      providers: [httpMock]
    });

    httpMock.useValue.get.and.returnValue(of({name: 'Name', id: '1', timezone: 'Australia/Brisbane'} as User));
    service = TestBed.inject(AuthenticationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
