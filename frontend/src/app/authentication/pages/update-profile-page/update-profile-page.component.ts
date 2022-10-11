import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {User} from "../../models/user";
import {AuthenticationService} from "../../services/authentication.service";
import {UpdateUserRequest} from "../../models/update-user-request";
import {MessageService} from "../../../shared/services/message.service";
import {UpdateCredentialsRequest} from "../../models/update-credentials-request";

@Component({
  selector: 'mediminder-update-profile-page',
  templateUrl: './update-profile-page.component.html',
  styleUrls: ['./update-profile-page.component.scss']
})
export class UpdateProfilePageComponent implements OnInit, OnDestroy {
  timezones: string[] = [];
  user?: User;
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private service: AuthenticationService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.initializeUser();
    this.initializeTimezones();
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  updateProfile(request: UpdateUserRequest): void {
    const next = (user: User) => {
      this.user = user;
      this.messageService.showSuccess(`User information successfully updated`);
    };
    const error = this.messageService.errorHandler(`Updating user information failed due to an unexpected error`);
    this.service.updateUser(request).subscribe({next, error});
  }

  updateCredentials(request: UpdateCredentialsRequest): void {
    const next = (user: User) => {
      this.user = user;
      this.messageService.showSuccess(`Credentials successfully updated`);
    };
    const error = this.messageService.errorHandler(`Updating credentials failed due to an unexpected error`);
    this.service.updateCredentials(request).subscribe({next, error});
  }

  private initializeUser(): void {
    this.service
      .getCurrentUser()
      .pipe(takeUntil(this.componentDestroyed))
      .subscribe(user => this.user = user);
  }

  private initializeTimezones(): void {
    this.service
      .findAllAvailableTimezones()
      .pipe(takeUntil(this.componentDestroyed))
      .subscribe(timezones => this.timezones = timezones);
  }
}
