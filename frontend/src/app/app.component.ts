import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from "./authentication/services/authentication.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  showNavigation: boolean = false;
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.authenticationService
      .isLoggedIn()
      .pipe(takeUntil(this.componentDestroyed))
      .subscribe(isLoggedIn => this.showNavigation = isLoggedIn);
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }
}
