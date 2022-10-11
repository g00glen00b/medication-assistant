import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfileFormComponent } from './update-profile-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";

describe('UpdateProfileFormComponent', () => {
  let component: UpdateProfileFormComponent;
  let fixture: ComponentFixture<UpdateProfileFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateProfileFormComponent ],
      imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, NoopAnimationsModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProfileFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
