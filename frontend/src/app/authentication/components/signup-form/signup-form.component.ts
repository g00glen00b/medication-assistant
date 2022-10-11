import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AbstractControl, AsyncValidatorFn, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication.service";
import {map, switchMap, timer} from "rxjs";
import {CreateUserRequest} from "../../models/create-user-request";

@Component({
  selector: 'mediminder-signup-form',
  templateUrl: './signup-form.component.html',
  styleUrls: ['./signup-form.component.scss']
})
export class SignupFormComponent implements OnInit {
  form!: FormGroup;
  timezones: string[] = [];
  @Output()
  onSignup: EventEmitter<CreateUserRequest> = new EventEmitter<CreateUserRequest>();

  constructor(private formBuilder: FormBuilder, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.initializeForm();
    this.initializeTimezones();
  }

  submit() {
    if (this.form.valid) {
      const email: string = this.form.get('email')?.value;
      const name: string = this.form.get('name')?.value;
      const password: string = this.form.get('passwords')?.get('password')?.value;
      const timezone: string = this.form.get('timezone')?.value;
      const request: CreateUserRequest = {email, name, password, timezone};
      this.onSignup.emit(request);
    }
  }

  private initializeForm(): void {
    this.form = this.formBuilder.group({
      email: this.formBuilder.control('', [Validators.required, Validators.email], [this.emailUnique()]),
      name: this.formBuilder.control('', [Validators.maxLength(256)]),
      passwords: this.formBuilder.group({
        password: this.formBuilder.control('', [Validators.required]),
        repeatPassword: this.formBuilder.control('', [Validators.required])
      }, {validators: [this.samePassword('password', 'repeatPassword')]}),
      timezone: this.formBuilder.control(''),
    });
  }

  private initializeTimezones(): void {
    this.authenticationService.findAllAvailableTimezones().subscribe(timezones => {
      this.timezones = timezones;
      this.initializeUserTimezone();
    });
  }

  private initializeUserTimezone(): void {
    const timezone: string = Intl.DateTimeFormat().resolvedOptions().timeZone;
    if (this.timezones.indexOf(timezone) >= 0) {
      this.form.get('timezone')?.setValue(timezone);
    }
  }

  private samePassword(field1: string, field2: string): ValidatorFn {
    return (group: AbstractControl) => {
      const value1: string = group.get(field1)!.value;
      const value2: string = group.get(field2)!.value;
      return value1 === value2 ? null : {samePassword: true};
    };
  }

  private emailUnique(): AsyncValidatorFn {
    return (control: AbstractControl) => timer(300).pipe(
        switchMap(() => this.authenticationService.existsByEmail(control!.value)),
        map(exists => exists ? {emailUnique: true} : null));
  }
}
