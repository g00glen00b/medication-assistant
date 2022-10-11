import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {UpdateCredentialsRequest} from "../../models/update-credentials-request";

@Component({
  selector: 'mediminder-update-credentials-form',
  templateUrl: './update-credentials-form.component.html',
  styleUrls: ['./update-credentials-form.component.scss']
})
export class UpdateCredentialsFormComponent implements OnInit {
  form!: FormGroup;
  @Output()
  onUpdate: EventEmitter<UpdateCredentialsRequest> = new EventEmitter<UpdateCredentialsRequest>();

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initializeForm();
  }

  submit(): void {
    if (this.form.valid) {
      const oldPassword: string = this.form.get('oldPassword')?.value;
      const newPassword: string = this.form.get('newPasswords')?.get('password')?.value;
      this.onUpdate.emit({oldPassword, newPassword});
    }
  }

  private initializeForm(): void {
    this.form = this.formBuilder.group({
      oldPassword: this.formBuilder.control('', [Validators.required]),
      newPasswords: this.formBuilder.group({
        password: this.formBuilder.control('', [Validators.required]),
        repeatPassword: this.formBuilder.control('', [Validators.required])
      }, {validators: [this.samePassword('password', 'repeatPassword')]}),
    });
  }

  private samePassword(field1: string, field2: string): ValidatorFn {
    return (group: AbstractControl) => {
      const value1: string = group.get(field1)!.value;
      const value2: string = group.get(field2)!.value;
      return value1 === value2 ? null : {samePassword: true};
    };
  }
}
