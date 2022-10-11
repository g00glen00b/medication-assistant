import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../models/user";
import {UpdateUserRequest} from "../../models/update-user-request";

@Component({
  selector: 'mediminder-update-profile-form',
  templateUrl: './update-profile-form.component.html',
  styleUrls: ['./update-profile-form.component.scss']
})
export class UpdateProfileFormComponent implements OnInit, OnChanges {
  form!: FormGroup;
  @Input()
  timezones: string[] = [];
  @Input()
  user!: User;
  @Output()
  onUpdate: EventEmitter<UpdateUserRequest> = new EventEmitter<UpdateUserRequest>();

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initializeForm();
    this.initializeFormFields();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.initializeFormFields();
  }

  submit(): void {
    if (this.form.valid) {
      const name: string | undefined = this.form.get('name')?.value;
      const timezone: string = this.form.get('timezone')!.value;
      this.onUpdate.emit({name, timezone});
    }
  }

  private initializeForm(): void {
    this.form = this.formBuilder.group({
      name: this.formBuilder.control('', [Validators.maxLength(256)]),
      timezone: this.formBuilder.control('', [Validators.required]),
    });
  }

  private initializeFormFields(): void {
    if (this.form != null && this.user != null) {
      this.form.get('name')!.setValue(this.user.name);
      this.form.get('timezone')!.setValue(this.user.timezone);
    }
  }

}
