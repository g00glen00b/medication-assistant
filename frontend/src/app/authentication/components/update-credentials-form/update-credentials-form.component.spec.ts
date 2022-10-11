import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCredentialsFormComponent } from './update-credentials-form.component';
import {ReactiveFormsModule} from "@angular/forms";

describe('UpdateCredentialsFormComponent', () => {
  let component: UpdateCredentialsFormComponent;
  let fixture: ComponentFixture<UpdateCredentialsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateCredentialsFormComponent ],
      imports: [ReactiveFormsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateCredentialsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
