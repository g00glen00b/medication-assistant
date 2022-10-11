import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, catchError, filter, map, Observable, tap, throwError} from "rxjs";
import {AuthenticationState} from "../models/authentication-state";
import {User} from "../models/user";
import {Credentials} from "../models/credentials";
import {CreateUserRequest} from "../models/create-user-request";
import {UpdateUserRequest} from "../models/update-user-request";
import {UpdateCredentialsRequest} from "../models/update-credentials-request";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private state: BehaviorSubject<AuthenticationState> = new BehaviorSubject<AuthenticationState>({initialized: false});

  constructor(private httpClient: HttpClient) {
    this.findCurrentUser().subscribe();
  }

  login(credentials: Credentials) {
    const headers: HttpHeaders = new HttpHeaders({
      Authorization: `Basic ${window.btoa(`${credentials.email}:${credentials.password}`)}`
    });
    return this.httpClient
        .get<User>(`./api/user/current`, {headers, withCredentials: true})
        .pipe(this.updateState());
  }

  getCurrentUser(): Observable<User> {
    return this.state.pipe(
        filter(({initialized}) => initialized),
        filter(({user}) => user != null),
        map(({user}) => user as User),
    );
  }

  signup(request: CreateUserRequest) {
    return this.httpClient.post<User>(`./api/user`, request);
  }

  isLoggedIn(): Observable<boolean> {
    return this.state.pipe(
      filter(({initialized}) => initialized),
      map(({user}) => user != null)
    );
  }

  existsByEmail(email: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`./api/user/email/exists?email=${email}`);
  }

  logout(): Observable<void> {
    return this.httpClient
      .post<void>(`./api/logout`, {})
      .pipe(tap(() => this.state.next({initialized: true})));
  }

  updateUser(request: UpdateUserRequest): Observable<User> {
    return this.httpClient
      .put<User>(`./api/user`, request)
      .pipe(this.updateState(false));
  }

  updateCredentials(request: UpdateCredentialsRequest): Observable<User> {
    return this.httpClient
      .put<User>(`./api/user/credentials`, request)
      .pipe(this.updateState(false));
  }

  findAllAvailableTimezones(): Observable<string[]> {
    return this.httpClient.get<string[]>(`./api/user/timezone`);
  }

  private findCurrentUser() {
    return this.httpClient
        .get<User>(`./api/user/current`, {withCredentials: true})
        .pipe(this.updateState());
  }

  private updateState(logoutOnError: boolean = true) {
    return (source: Observable<User>) => source.pipe(
      tap(user => this.state.next({initialized: true, user})),
      catchError((error: HttpErrorResponse) => {
        if (error.status == 401 && logoutOnError) this.state.next({initialized: true});
        return throwError(() => error);
      }));
  }
}