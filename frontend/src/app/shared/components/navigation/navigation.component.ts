import {Component} from '@angular/core';
import {AuthenticationService} from "../../../authentication/services/authentication.service";
import {Router} from "@angular/router";
import {MessageService} from "../../services/message.service";

@Component({
  selector: 'mediminder-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {

  constructor(
      private authenticationService: AuthenticationService,
      private messageService: MessageService,
      private router: Router) {
  }

  logout(): void {
    this.authenticationService
        .logout()
        .subscribe(() => {
          this.messageService.showSuccess(`You're successfully logged out`);
          this.router.navigate([`/authentication/login`]);
        });
  }
}
