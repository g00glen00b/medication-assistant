import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailabilityCardComponent } from './availability-card.component';

describe('AvailabilityCardComponent', () => {
  let component: AvailabilityCardComponent;
  let fixture: ComponentFixture<AvailabilityCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvailabilityCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvailabilityCardComponent);
    component = fixture.componentInstance;
    const medication = {id: '2', name: 'Hydrocortisone', quantityType: {id: '3', name: 'ml'}};
    component.availability = {id: '1', medication, quantity: 1, initialQuantity: 10, expiryDate: '2022-10-10'};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
