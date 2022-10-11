import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Credentials} from "../../models/credentials";

@Component({
  selector: 'mediminder-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
  form!: FormGroup;
  @Output()
  onCredentials: EventEmitter<Credentials> = new EventEmitter<Credentials>();

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: this.formBuilder.control('', [Validators.required, Validators.email, Validators.maxLength(256)]),
      password: this.formBuilder.control('', [Validators.required])
    });
  }

  submit() {
    if (this.form.valid) {
      const email: string = this.form.get('email')!.value;
      const password: string = this.form.get('password')!.value;
      const credentials: Credentials = {email, password};
      this.onCredentials.next(credentials);
    }
  }

}
