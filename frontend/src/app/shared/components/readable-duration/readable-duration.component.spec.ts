import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadableDurationComponent } from './readable-duration.component';

describe('ReadableDurationComponent', () => {
  let component: ReadableDurationComponent;
  let fixture: ComponentFixture<ReadableDurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReadableDurationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReadableDurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
