import { TestBed } from '@angular/core/testing';

import { AuthenticationGuard } from './authentication.guard';
import {provideMock} from "../../../../test/jasmine-utils";
import {AuthenticationService} from "./authentication.service";

describe('AuthenticationGuard', () => {
  let guard: AuthenticationGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(AuthenticationService)]
    });
    guard = TestBed.inject(AuthenticationGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
