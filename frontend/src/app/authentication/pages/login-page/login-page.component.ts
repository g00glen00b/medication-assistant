import {Component, OnDestroy} from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {Credentials} from "../../models/credentials";
import {MessageService} from "../../../shared/services/message.service";
import {Router} from "@angular/router";
import {User} from "../../models/user";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'mediminder-login',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnDestroy {
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private authenticationService: AuthenticationService, private messageService: MessageService, private router: Router) { }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  login(credentials: Credentials) {
    const next = (user: User) => {
      if (user.name.length > 0) {
        this.messageService.showSuccess(`Welcome back ${user.name}!`);
      } else {
        this.messageService.showSuccess(`Welcome back!`);
      }
      this.router.navigate([`/`]);
    };
    const error = this.messageService.errorHandler(`E-mail or password are incorrect`);
    this.authenticationService
        .login(credentials)
        .pipe(takeUntil(this.componentDestroyed))
        .subscribe({next, error});
  }
}
