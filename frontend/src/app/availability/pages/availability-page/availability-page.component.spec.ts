import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailabilityPageComponent } from './availability-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {AvailabilityService} from "../../services/availability.service";
import {ConfirmationService} from "../../../shared/services/confirmation.service";
import {MessageService} from "../../../shared/services/message.service";

describe('AvailabilityPageComponent', () => {
  let component: AvailabilityPageComponent;
  let fixture: ComponentFixture<AvailabilityPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvailabilityPageComponent ],
      providers: [provideMock(AvailabilityService), provideMock(ConfirmationService), provideMock(MessageService)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvailabilityPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
