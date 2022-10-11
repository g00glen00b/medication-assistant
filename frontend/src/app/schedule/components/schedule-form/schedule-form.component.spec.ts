import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleFormComponent } from './schedule-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {provideMock} from "../../../../../test/jasmine-utils";
import {MedicationService} from "../../../medication/services/medication.service";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {EMPTY_PAGE_INFO, PageInfo} from "../../../shared/models/page-info";
import {Medication} from "../../../medication/models/medication";
import {of} from "rxjs";

describe('ScheduleFormComponent', () => {
  let component: ScheduleFormComponent;
  let fixture: ComponentFixture<ScheduleFormComponent>;
  let medicationServiceMock;

  beforeEach(async () => {
    medicationServiceMock = provideMock(MedicationService);
    await TestBed.configureTestingModule({
      declarations: [ ScheduleFormComponent ],
      imports: [ ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatSelectModule, MatInputModule, NoopAnimationsModule, MatDatepickerModule, MatNativeDateModule ],
      providers: [medicationServiceMock]
    })
    .compileComponents();

    medicationServiceMock.useValue.findAll.and.returnValue(of(EMPTY_PAGE_INFO as PageInfo<Medication>));
    fixture = TestBed.createComponent(ScheduleFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
