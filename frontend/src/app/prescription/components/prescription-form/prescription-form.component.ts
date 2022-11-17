import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {addYears} from "date-fns";
import {CalculatePrescriptionRequest} from "../../models/calculate-prescription-request";

@Component({
  selector: 'mediminder-prescription-form',
  templateUrl: './prescription-form.component.html',
  styleUrls: ['./prescription-form.component.scss']
})
export class PrescriptionFormComponent implements OnInit {
  form!: FormGroup;
  maximumDate!: Date;
  @Output()
  onCalculate: EventEmitter<CalculatePrescriptionRequest> = new EventEmitter<CalculatePrescriptionRequest>();

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      date: this.formBuilder.control(null, [Validators.required])
    });
    this.maximumDate = addYears(new Date(), 2);
  }

  submit(): void {
    if (this.form.valid) {
      const date: Date = this.form.get('date')!.value;
      this.onCalculate.emit({date});
    }
  }

}
