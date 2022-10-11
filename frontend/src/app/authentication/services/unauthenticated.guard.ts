import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {map, Observable, tap} from 'rxjs';
import {AuthenticationService} from "./authentication.service";

@Injectable({
  providedIn: 'root'
})
export class UnauthenticatedGuard implements CanActivate {
  constructor(private service: AuthenticationService, private router: Router) {
  }

  canActivate(
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.service
        .isLoggedIn()
        .pipe(
            this.redirectToHomeIfAuthenticated(),
            map(isLoggedIn => !isLoggedIn));
  }

  private redirectToHomeIfAuthenticated() {
    return (source: Observable<boolean>) => source.pipe(tap(isLoggedIn => {
      if (isLoggedIn) this.router.navigate([`/`]);
    }));
  }
  
}
