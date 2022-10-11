import {Component, OnDestroy, OnInit} from '@angular/core';
import {BehaviorSubject, filter, mergeMap, Subject, takeUntil} from "rxjs";
import {PageRequest} from "../../../shared/models/page-request";
import {Availability} from "../../models/availability";
import {AvailabilityService} from "../../services/availability.service";
import {ConfirmationService} from "../../../shared/services/confirmation.service";
import {MessageService} from "../../../shared/services/message.service";
import {ConfirmationInfo} from "../../../shared/models/confirmation-info";
import {Router} from "@angular/router";

@Component({
  selector: 'mediminder-availability-page',
  templateUrl: './availability-page.component.html',
  styleUrls: ['./availability-page.component.scss']
})
export class AvailabilityPageComponent implements OnInit, OnDestroy {
  allAvailabilities: Availability[] = [];
  private pageRequest: PageRequest = PageRequest.firstSortedPage(12, 'expiryDate,asc');
  private totalElements: number = 0;
  private pageRequestSubject: BehaviorSubject<PageRequest> = new BehaviorSubject<PageRequest>(this.pageRequest);
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(
    private service: AvailabilityService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private router: Router) { }

  ngOnInit(): void {
    this.initializeAvailabilities();
    this.initializePageRequest();
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  findNextPage(): void {
    const nextPage: PageRequest = this.pageRequest.next();
    const [startIndex] = nextPage.calculateIndexRange();
    if (startIndex <= this.totalElements) {
      this.pageRequestSubject.next(this.pageRequest.next());
    }
  }

  updateAvailability(availability: Availability): void {
    this.router.navigate([`/inventory/update/${availability.id}`]);
  }

  deleteAvailability(availability: Availability): void {
    const confirmationInfo: ConfirmationInfo = {message: `Are you sure you want to delete ${availability.medication.name}?`};
    const next = () => {
      this.allAvailabilities = this.allAvailabilities.filter(({id}) => id != availability.id);
      this.messageService.showSuccess(`Medication ${availability.medication.name} successfully removed from your inventory`)
    };
    const error = this.messageService.errorHandler(`Deleting the ${availability.medication.name} from your inventory failed due to an unexpected error`);
    this.confirmationService
      .confirm(confirmationInfo)
      .pipe(
        takeUntil(this.componentDestroyed),
        filter(result => result),
        mergeMap(() => this.service.delete(availability.id)))
      .subscribe({next, error});
  }

  private initializeAvailabilities() {
    this.pageRequestSubject
      .pipe(
        takeUntil(this.componentDestroyed),
        mergeMap(pageRequest => this.service.findAll(pageRequest)))
      .subscribe(pageInfo => {
        this.totalElements = pageInfo.totalElements;
        this.allAvailabilities = [...this.allAvailabilities, ...pageInfo.content];
      });
  }

  private initializePageRequest() {
    this.pageRequestSubject
      .pipe(takeUntil(this.componentDestroyed))
      .subscribe(pageRequest => this.pageRequest = pageRequest);
  }
}
