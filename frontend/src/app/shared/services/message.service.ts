import { Injectable } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private snackbar: MatSnackBar) { }

  showError(message: string) {
    return this.snackbar.open(message, 'HIDE', {panelClass: ['warn-snackbar']});
  }

  errorHandler(defaultMessage: string) {
    return (error: Error | HttpErrorResponse) => {
      if ('error' in error && 'message' in error.error) {
        this.showError(error.error.message);
      } else {
        this.showError(defaultMessage);
      }
    };
  }

  showSuccess(message: string) {
    return this.snackbar.open(message, 'HIDE', {panelClass: ['success-snackbar']});
  }
}
