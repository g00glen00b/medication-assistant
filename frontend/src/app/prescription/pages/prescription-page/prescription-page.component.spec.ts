import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrescriptionPageComponent } from './prescription-page.component';

describe('PrescriptionPageComponent', () => {
  let component: PrescriptionPageComponent;
  let fixture: ComponentFixture<PrescriptionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrescriptionPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrescriptionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
