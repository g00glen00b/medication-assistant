import { TestBed } from '@angular/core/testing';

import { UnauthenticatedGuard } from './unauthenticated.guard';
import {provideMock} from "../../../../test/jasmine-utils";
import {AuthenticationService} from "./authentication.service";

describe('UnauthenticatedGuard', () => {
  let guard: UnauthenticatedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMock(AuthenticationService)]
    });
    guard = TestBed.inject(UnauthenticatedGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
