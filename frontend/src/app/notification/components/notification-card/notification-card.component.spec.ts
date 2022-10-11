import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NotificationCardComponent} from './notification-card.component';
import {NotificationType} from "../../models/notification";

describe('NotificationCardComponent', () => {
  let component: NotificationCardComponent;
  let fixture: ComponentFixture<NotificationCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotificationCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationCardComponent);
    component = fixture.componentInstance;
    component.notification = {id: '1', message: 'Message', type: NotificationType.INFO, createdDate: '2022-10-10'};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
