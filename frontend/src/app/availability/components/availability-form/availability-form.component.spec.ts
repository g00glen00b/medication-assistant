import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailabilityFormComponent } from './availability-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {provideMock} from "../../../../../test/jasmine-utils";
import {MedicationService} from "../../../medication/services/medication.service";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {of} from "rxjs";
import {EMPTY_PAGE_INFO, PageInfo} from "../../../shared/models/page-info";
import {QuantityType} from "../../../medication/models/quantity-type";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {Medication} from "../../../medication/models/medication";

describe('AvailabilityFormComponent', () => {
  let component: AvailabilityFormComponent;
  let fixture: ComponentFixture<AvailabilityFormComponent>;
  let medicationServiceMock;

  beforeEach(async () => {
    medicationServiceMock = provideMock(MedicationService);
    await TestBed.configureTestingModule({
      declarations: [ AvailabilityFormComponent ],
      imports: [ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule, MatSelectModule, NoopAnimationsModule],
      providers: [medicationServiceMock]
    })
    .compileComponents();

    medicationServiceMock.useValue.findAllQuantityTypes.and.returnValue(of(EMPTY_PAGE_INFO as PageInfo<QuantityType>));
    medicationServiceMock.useValue.findAll.and.returnValue(of(EMPTY_PAGE_INFO as PageInfo<Medication>));
    fixture = TestBed.createComponent(AvailabilityFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
