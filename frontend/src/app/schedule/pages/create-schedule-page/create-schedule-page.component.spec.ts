import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSchedulePageComponent } from './create-schedule-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {ScheduleService} from "../../services/schedule.service";
import {MessageService} from "../../../shared/services/message.service";

describe('CreateSchedulePageComponent', () => {
  let component: CreateSchedulePageComponent;
  let fixture: ComponentFixture<CreateSchedulePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateSchedulePageComponent ],
      providers: [provideMock(ScheduleService), provideMock(MessageService)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateSchedulePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
