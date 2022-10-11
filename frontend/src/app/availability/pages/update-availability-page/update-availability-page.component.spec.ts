import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAvailabilityPageComponent } from './update-availability-page.component';
import {provideMock} from "../../../../../test/jasmine-utils";
import {AvailabilityService} from "../../services/availability.service";
import {MessageService} from "../../../shared/services/message.service";
import {ActivatedRoute, convertToParamMap} from "@angular/router";
import {of} from "rxjs";

describe('UpdateAvailabilityPageComponent', () => {
  let component: UpdateAvailabilityPageComponent;
  let fixture: ComponentFixture<UpdateAvailabilityPageComponent>;
  let activatedRouteMock;

  beforeEach(async () => {
    activatedRouteMock = provideMock(ActivatedRoute);
    await TestBed.configureTestingModule({
      declarations: [ UpdateAvailabilityPageComponent ],
      providers: [provideMock(AvailabilityService), provideMock(MessageService), activatedRouteMock]
    })
    .compileComponents();

    activatedRouteMock.useValue.paramMap = of(convertToParamMap({id: '1'}));
    fixture = TestBed.createComponent(UpdateAvailabilityPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
