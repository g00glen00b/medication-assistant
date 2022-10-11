import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfilePageComponent } from './update-profile-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {AuthenticationService} from "../../services/authentication.service";
import {MessageService} from "../../../shared/services/message.service";
import {of} from "rxjs";
import {User} from "../../models/user";

describe('UpdateProfilePageComponent', () => {
  let component: UpdateProfilePageComponent;
  let fixture: ComponentFixture<UpdateProfilePageComponent>;
  let authenticationServiceMock;

  beforeEach(async () => {
    authenticationServiceMock = provideMock(AuthenticationService);
    await TestBed.configureTestingModule({
      declarations: [ UpdateProfilePageComponent ],
      providers: [authenticationServiceMock, provideMock(MessageService)]
    })
    .compileComponents();

    authenticationServiceMock.useValue.getCurrentUser.and.returnValue(of({id: '1', timezone: 'UTC', name: 'Zuhur Shadhiyah Attia'} as User));
    authenticationServiceMock.useValue.findAllAvailableTimezones.and.returnValue(of([]));
    fixture = TestBed.createComponent(UpdateProfilePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
