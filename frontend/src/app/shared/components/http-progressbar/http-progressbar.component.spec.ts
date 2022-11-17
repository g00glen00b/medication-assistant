import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HttpProgressbarComponent } from './http-progressbar.component';

describe('HttpProgressbarComponent', () => {
  let component: HttpProgressbarComponent;
  let fixture: ComponentFixture<HttpProgressbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HttpProgressbarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HttpProgressbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
