import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPageComponent } from './dashboard-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {ScheduleService} from "../../../schedule/services/schedule.service";
import {NotificationService} from "../../../notification/services/notification.service";
import {AuthenticationService} from "../../../authentication/services/authentication.service";
import {MessageService} from "../../../shared/services/message.service";
import {of} from "rxjs";
import {User} from "../../../authentication/models/user";

describe('DashboardPageComponent', () => {
  let component: DashboardPageComponent;
  let fixture: ComponentFixture<DashboardPageComponent>;
  let authenticationServiceMock;

  beforeEach(async () => {
    authenticationServiceMock = provideMock(AuthenticationService);
    await TestBed.configureTestingModule({
      declarations: [ DashboardPageComponent ],
      providers: [provideMock(ScheduleService), provideMock(NotificationService), authenticationServiceMock, provideMock(MessageService)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardPageComponent);
    authenticationServiceMock.useValue.getCurrentUser.and.returnValue(of({id: '1', name: 'Quinzia Iadanza', timezone: 'UTC'} as User));
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
