import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Medication} from "../../../medication/models/medication";
import {MedicationService} from "../../../medication/services/medication.service";
import {debounceTime, filter, map, mergeMap, startWith, Subject, takeUntil} from "rxjs";
import {QuantityType} from "../../../medication/models/quantity-type";
import {format, parseISO} from 'date-fns';
import {CreateAvailabilityRequest} from "../../models/create-availability-request";
import {Availability} from "../../models/availability";
import {FIRST_TEN_RESULTS} from "../../../shared/models/page-request";


@Component({
  selector: 'mediminder-availability-form',
  templateUrl: './availability-form.component.html',
  styleUrls: ['./availability-form.component.scss']
})
export class AvailabilityFormComponent implements OnInit, OnChanges, OnDestroy {
  @Input()
  availability?: Availability;
  @Input()
  buttonText: 'ADD' | 'UPDATE' = 'ADD';
  @Output()
  onAvailability: EventEmitter<CreateAvailabilityRequest> = new EventEmitter<CreateAvailabilityRequest>();
  @Output()
  onCancel: EventEmitter<void> = new EventEmitter<void>();
  form!: FormGroup;
  suggestedMedications: Medication[] = [];
  quantityTypes: QuantityType[] = [];
  minimumExpiryDate: Date = new Date();

  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private formBuilder: FormBuilder, private medicationService: MedicationService) { }

  ngOnInit(): void {
    this.initializeForm();
    this.initializeFormValues();
    this.initializeButtonText();
    this.initializeFormDisableState();
    this.initializeMedicationAutocomplete();
    this.initializeQuantityTypes();
    this.initializeQuantityTypeFromMedication();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.initializeFormValues();
    this.initializeButtonText();
    this.initializeFormDisableState();
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  getMedicationName(medication: Medication): string {
    return medication.name;
  }

  isQuantityTypeIdEqual(quantityType1: QuantityType | undefined, quantityType2: QuantityType | undefined): boolean {
    return quantityType1 != null && quantityType2 != null && quantityType1.id == quantityType2.id;
  }

  submit(): void {
    if (this.form.valid) {
      const medicationValue: Medication | string = this.form.get('medication')!.value;
      const medicationName: string = typeof medicationValue == 'string' ? medicationValue : medicationValue.name;
      const quantityTypeId: string = this.form.get('quantityType')!.value.id;
      const quantity: number = this.form.get('quantities')!.get('quantity')?.value;
      const initialQuantity: number = this.form.get('quantities')!.get('initialQuantity')!.value;
      const expiryDateValue: Date = this.form.get('expiryDate')!.value;
      const expiryDate: string = format(expiryDateValue, 'yyyy-MM-dd');
      const request: CreateAvailabilityRequest = {medicationName, quantityTypeId, quantity, initialQuantity, expiryDate};
      this.onAvailability.emit(request);
    }
  }

  cancel(): void {
    this.onCancel.emit();
  }

  private initializeForm() {
    this.form = this.formBuilder.group({
      medication: this.formBuilder.control('', [Validators.required, Validators.maxLength(256)]),
      quantityType: this.formBuilder.control(null, [Validators.required]),
      quantities: this.formBuilder.group({
        quantity: this.formBuilder.control(0, [Validators.required, Validators.min(0)]),
        initialQuantity: this.formBuilder.control(0, [Validators.required, Validators.min(0)])
      }, {validators: [this.quantityLessThanInitial('quantity', 'initialQuantity')]}),
      expiryDate: this.formBuilder.control(new Date(), [Validators.required])
    });
  }

  private initializeFormValues() {
    if (this.form == null) return;
    const medication = this.availability?.medication || '';
    const quantityType = this.availability?.medication?.quantityType || '';
    const quantity = this.availability?.quantity || 0;
    const initialQuantity = this.availability?.initialQuantity || 0;
    const expiryDate = this.availability?.expiryDate == null ? new Date() : parseISO(this.availability.expiryDate);
    this.form.get('medication')?.setValue(medication);
    this.form.get('quantityType')?.setValue(quantityType);
    this.form.get('quantities')?.get('quantity')?.setValue(quantity);
    this.form.get('quantities')?.get('initialQuantity')?.setValue(initialQuantity);
    this.form.get('expiryDate')?.setValue(expiryDate);
  }

  private initializeButtonText() {
    this.buttonText = this.availability == null ? 'ADD' : 'UPDATE';
  }

  private initializeFormDisableState() {
    if (this.form == null) return;
    if (this.availability != null) {
      this.form.get('medication')!.disable();
      this.form.get('quantityType')!.disable();
    } else {
      this.form.get('medication')!.enable();
      this.form.get('quantityType')!.enable();
    }
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

  private initializeQuantityTypes() {
    this.medicationService.findAllQuantityTypes(FIRST_TEN_RESULTS).pipe(
      takeUntil(this.componentDestroyed),
      map(({content}) => content)
    ).subscribe(quantityTypes => {
      this.quantityTypes = quantityTypes;
      this.form.get('quantityType')?.setValue(quantityTypes[0]);
    });
  }

  private initializeQuantityTypeFromMedication() {
    this.form.get('medication')?.valueChanges.subscribe(medication => {
      if (typeof medication == 'object' && 'quantityType' in medication) {
        this.form.get('quantityType')?.setValue(medication.quantityType);
        this.form.get('quantityType')?.disable();
      } else {
        this.form.get('quantityType')?.enable();
      }
    });
  }

  private quantityLessThanInitial(quantity: string, initialQuantity: string): ValidatorFn {
    return (group: AbstractControl) => {
      const quantityValue: number | undefined = group.get(quantity)!.value;
      const initialQuantityValue: number | undefined = group.get(initialQuantity)!.value;
      const hasQuantities: boolean = quantityValue != null && initialQuantityValue != null;
      return hasQuantities && initialQuantityValue! < quantityValue! ? {quantityComparison: true} : null;
    };
  }
}
