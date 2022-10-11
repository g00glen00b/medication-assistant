import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulePageComponent } from './schedule-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {ScheduleService} from "../../services/schedule.service";
import {MatDialog} from "@angular/material/dialog";
import {MessageService} from "../../../shared/services/message.service";

describe('SchedulePageComponent', () => {
  let component: SchedulePageComponent;
  let fixture: ComponentFixture<SchedulePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchedulePageComponent ],
      providers: [provideMock(ScheduleService), provideMock(MatDialog), provideMock(MessageService)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchedulePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
