import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateSchedulePageComponent } from './update-schedule-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {ScheduleService} from "../../services/schedule.service";
import {MessageService} from "../../../shared/services/message.service";
import {ActivatedRoute, convertToParamMap} from "@angular/router";
import {of} from "rxjs";

describe('UpdateSchedulePageComponent', () => {
  let component: UpdateSchedulePageComponent;
  let fixture: ComponentFixture<UpdateSchedulePageComponent>;

  beforeEach(async () => {
    let activatedRouteMock = provideMock(ActivatedRoute);
    await TestBed.configureTestingModule({
      declarations: [ UpdateSchedulePageComponent ],
      providers: [provideMock(ScheduleService), provideMock(MessageService), activatedRouteMock]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateSchedulePageComponent);
    activatedRouteMock.useValue.paramMap = of(convertToParamMap({id: '1'}));
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
