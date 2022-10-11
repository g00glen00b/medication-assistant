import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAvailabilityPageComponent } from './create-availability-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {AvailabilityService} from "../../services/availability.service";
import {MessageService} from "../../../shared/services/message.service";

describe('CreateAvailabilityPageComponent', () => {
  let component: CreateAvailabilityPageComponent;
  let fixture: ComponentFixture<CreateAvailabilityPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateAvailabilityPageComponent ],
      providers: [provideMock(AvailabilityService), provideMock(MessageService)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateAvailabilityPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
