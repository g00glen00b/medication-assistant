import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupFormComponent } from './signup-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {provideMock} from "../../../../../test/jasmine-utils";
import {AuthenticationService} from "../../services/authentication.service";
import {of} from "rxjs";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";

describe('SignupFormComponent', () => {
  let component: SignupFormComponent;
  let fixture: ComponentFixture<SignupFormComponent>;
  let authenticationService;

  beforeEach(async () => {
    authenticationService = provideMock(AuthenticationService);
    await TestBed.configureTestingModule({
      declarations: [ SignupFormComponent ],
      imports: [ReactiveFormsModule, MatFormFieldModule, MatSelectModule, MatInputModule, NoopAnimationsModule],
      providers: [authenticationService]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignupFormComponent);
    authenticationService.useValue.findAllAvailableTimezones.and.returnValue(of([]));
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
