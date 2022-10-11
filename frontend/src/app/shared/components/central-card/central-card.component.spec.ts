import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CentralCardComponent } from './central-card.component';

describe('CentralCardComponent', () => {
  let component: CentralCardComponent;
  let fixture: ComponentFixture<CentralCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CentralCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CentralCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
