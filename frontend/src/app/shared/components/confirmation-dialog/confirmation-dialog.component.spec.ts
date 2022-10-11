import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import {provideMatDialogData, provideMock} from "../../../../../test/jasmine-utils";
import {MatDialogRef} from "@angular/material/dialog";
import {ConfirmationInfo} from "../../models/confirmation-info";

describe('ConfirmationDialogComponent', () => {
  let component: ConfirmationDialogComponent;
  let fixture: ComponentFixture<ConfirmationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmationDialogComponent ],
      providers: [provideMock(MatDialogRef), provideMatDialogData<ConfirmationInfo>({message: 'Message'})]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
