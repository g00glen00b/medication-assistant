import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {debounceTime, filter, map, mergeMap, startWith, Subject, takeUntil} from "rxjs";
import {FIRST_TEN_RESULTS} from "../../../shared/models/page-request";
import {MedicationService} from "../../../medication/services/medication.service";
import {Medication} from "../../../medication/models/medication";
import {CreateScheduleRequest} from "../../models/create-schedule-request";
import {Duration, format, formatISODuration, parseISO} from "date-fns";
import {IntervalType} from "../../models/interval-type";
import {Schedule} from "../../models/schedule";
import {parseAndNormalizeDuration} from "../../../shared/utilities/duration-utilities";

@Component({
  selector: 'mediminder-schedule-form',
  templateUrl: './schedule-form.component.html',
  styleUrls: ['./schedule-form.component.scss']
})
export class ScheduleFormComponent implements OnInit, OnDestroy {
  @Input()
  schedule?: Schedule;
  @Output()
  onSchedule: EventEmitter<CreateScheduleRequest> = new EventEmitter<CreateScheduleRequest>();
  @Output()
  onCancel: EventEmitter<void> = new EventEmitter<void>();
  buttonText: 'ADD' | 'UPDATE' = 'ADD';
  suggestedMedications: Medication[] = [];
  form!: FormGroup;
  intervalTypes: IntervalType[] = ['Days', 'Weeks', 'Months'];
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private formBuilder: FormBuilder, private medicationService: MedicationService) { }

  ngOnInit(): void {
    this.initializeForm();
    this.initializeMedicationAutocomplete();
    this.initializeFormValues();
    this.initializeButtonText();
    this.initializeFormDisableState();
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  submit(): void {
    if (this.form.valid) {
      const medicationId: string = this.form.get('medication')?.value.id;
      const startingAt = this.calculateISOStartingAt();
      const endingAtInclusive = this.calculateISOEndingAt();
      const interval = this.calculateISOInterval();
      const time: string = this.form.get('time')?.value;
      const description: string = this.form.get('description')?.value;
      const quantity: number = this.form.get('quantity')?.value;
      const request: CreateScheduleRequest = {medicationId, description, interval, time, startingAt, endingAtInclusive, quantity};
      this.onSchedule.emit(request);
    }
  }

  cancel(): void {
    this.onCancel.emit();
  }

  getMedicationName(medication: Medication): string {
    return medication.name;
  }

  private initializeForm(): void {
    this.form = this.formBuilder.group({
      medication: this.formBuilder.control('', [Validators.required, this.autocompleteSelected()]),
      quantity: this.formBuilder.control(0, [Validators.required, Validators.min(0)]),
      period: this.formBuilder.group({
        start: this.formBuilder.control(new Date(), [Validators.required]),
        end: this.formBuilder.control(null, []),
      }),
      description: this.formBuilder.control('', [Validators.maxLength(256)]),
      time: this.formBuilder.control('', [Validators.required]),
      interval: this.formBuilder.group({
        amount: this.formBuilder.control(1, [Validators.required, Validators.min(1)]),
        type: this.formBuilder.control('', [Validators.required])
      }),
    });
  }

  private initializeMedicationAutocomplete() {
    this.form.get('medication')?.valueChanges.pipe(
      startWith(''),
      takeUntil(this.componentDestroyed),
      filter(value => typeof value === 'string'),
      debounceTime(300),
      mergeMap(search => this.medicationService.findAll(search, FIRST_TEN_RESULTS)),
      map(({content}) => content)
    ).subscribe(medication => this.suggestedMedications = medication);
  }

  private calculateIntervalFormValues(value?: string): [number, IntervalType] {
    if (value != null) {
      const interval: Duration = parseAndNormalizeDuration(value);
      const {days, weeks, months} = interval;
      if (days != null && days > 0) return [days, 'Days'];
      if (weeks != null && weeks > 0) return [weeks, 'Weeks'];
      if (months != null && months > 0) return [months, 'Months'];
    }
    return [1, 'Days'];
  }

  private initializeFormValues() {
    if (this.form == null) return;
    const medication = this.schedule?.medication || '';
    const quantity = this.schedule?.quantity || 0;
    const startingAt = this.schedule?.period.startingAt == null ? new Date() : parseISO(this.schedule?.period.startingAt);
    const endingAt = this.schedule?.period.endingAtInclusive == null ? null : parseISO(this.schedule?.period.endingAtInclusive);
    const description = this.schedule?.description || '';
    const time = this.schedule?.time || '';
    const [intervalAmount, intervalType] = this.calculateIntervalFormValues(this.schedule?.interval);

    this.form.get('medication')?.setValue(medication);
    this.form.get('quantity')?.setValue(quantity);
    this.form.get('period')?.get('start')?.setValue(startingAt);
    this.form.get('period')?.get('end')?.setValue(endingAt);
    this.form.get('description')?.setValue(description);
    this.form.get('time')?.setValue(time);
    this.form.get('interval')?.get('amount')?.setValue(intervalAmount);
    this.form.get('interval')?.get('type')?.setValue(intervalType);
  }

  private initializeButtonText() {
    this.buttonText = this.schedule == null ? 'ADD' : 'UPDATE';
  }

  private initializeFormDisableState() {
    if (this.form == null) return;
    if (this.schedule != null) {
      this.form.get('medication')!.disable();
    } else {
      this.form.get('medication')!.enable();
    }
  }

  private autocompleteSelected(): ValidatorFn {
    return (control: AbstractControl) => {
      return control.value != null && typeof control.value === 'string' ? {autocompleteSelected: true} : null;
    };
  }

  private calculateISOInterval(): string {
    const intervalValue: number = this.form.get('interval')?.get('amount')?.value
    const intervalType: IntervalType = this.form.get('interval')?.get('type')?.value;
    const duration: Duration = this.calculateDuration(intervalValue, intervalType);
    const formattedDuration: string = formatISODuration(duration);
    return this.removeTimeFromISODuration(formattedDuration);
  }

  private removeTimeFromISODuration(isoDuration: string): string {
    return isoDuration.replace(/T.*$/, '');
  }

  private calculateDuration(value: number, type: IntervalType): Duration {
    switch (type) {
      case 'Days': return {days: value};
      case 'Weeks': return {days: value * 7};
      case 'Months': return {months: value};
      default: return {};
    }
  }

  private calculateISOStartingAt() {
    const startingAtDate: Date = this.form.get('period')?.get('start')?.value;
    return format(startingAtDate, 'yyyy-MM-dd');
  }

  private calculateISOEndingAt(): string | undefined {
    const endingAtDate: Date | undefined = this.form.get('period')?.get('end')?.value;
    return endingAtDate == undefined ? undefined : format(endingAtDate, 'yyyy-MM-dd');
  }

}
