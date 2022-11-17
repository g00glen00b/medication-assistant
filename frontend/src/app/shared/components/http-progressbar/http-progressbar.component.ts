import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpRequestService} from "../../services/http-request.service";
import {debounceTime, Subject, takeUntil} from "rxjs";

@Component({
  selector: 'mediminder-http-progressbar',
  templateUrl: './http-progressbar.component.html',
  styleUrls: ['./http-progressbar.component.scss']
})
export class HttpProgressbarComponent implements OnInit, OnDestroy {
  isLoading: boolean = false;
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(private httpRequestService: HttpRequestService) { }

  ngOnInit(): void {
    this.httpRequestService
      .observable()
      .pipe(
        takeUntil(this.componentDestroyed),
        debounceTime(300))
      .subscribe(requests => this.isLoading = requests.length > 0);
  }

  ngOnDestroy() {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }
}
