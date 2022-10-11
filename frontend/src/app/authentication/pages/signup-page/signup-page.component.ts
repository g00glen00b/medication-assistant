import {Component, OnDestroy} from '@angular/core';
import {CreateUserRequest} from "../../models/create-user-request";
import {AuthenticationService} from "../../services/authentication.service";
import {User} from "../../models/user";
import {Router} from "@angular/router";
import {MessageService} from "../../../shared/services/message.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'mediminder-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrls: ['./signup-page.component.scss']
})
export class SignupPageComponent implements OnDestroy {
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private authenticationService: AuthenticationService, private messageService: MessageService, private router: Router) { }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  signup(request: CreateUserRequest) {
    const next = (user: User) => {
      if (user.name.length > 0) {
        this.messageService.showSuccess(`Welcome ${user.name}! You can sign in now.`);
      } else {
        this.messageService.showSuccess(`Welcome! You can sign in now.`);
      }
      this.router.navigate([`/authentication/login`]);
    };
    const error = this.messageService.errorHandler(`Registration failed due to an unexpected error`);
    this.authenticationService
        .signup(request)
        .pipe(takeUntil(this.componentDestroyed))
        .subscribe({next, error});
  }
}
