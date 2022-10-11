import {Component, OnDestroy, OnInit} from '@angular/core';
import {AvailabilityService} from "../../services/availability.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {MessageService} from "../../../shared/services/message.service";
import {filter, map, mergeMap, Observable, Subject, take, takeUntil} from "rxjs";
import {Availability} from "../../models/availability";
import {CreateAvailabilityRequest} from "../../models/create-availability-request";
import {UpdateAvailabilityRequest} from "../../models/update-availability-request";

@Component({
  selector: 'mediminder-update-availability-page',
  templateUrl: './update-availability-page.component.html',
  styleUrls: ['./update-availability-page.component.scss']
})
export class UpdateAvailabilityPageComponent implements OnInit, OnDestroy {
  availability?: Availability;
  private componentDestroyed: Subject<void> = new Subject<void>();

  constructor(
      private service: AvailabilityService,
      private messageService: MessageService,
      private activatedRoute: ActivatedRoute,
      private router: Router) { }

  ngOnInit(): void {
    const next = (result: Availability) => this.availability = result;
    const error = this.messageService.errorHandler('This medication does not exist in your inventory. You may have deleted it earlier');
    this.activatedRoute.paramMap
      .pipe(
        this.mapParameterId(),
        take(1),
        takeUntil(this.componentDestroyed),
        mergeMap(id => this.service.findById(id)))
      .subscribe({next, error});
  }

  ngOnDestroy(): void {
    this.componentDestroyed.next();
    this.componentDestroyed.complete();
  }

  update(request: CreateAvailabilityRequest): void {
    const {quantity, initialQuantity, expiryDate} = request;
    const updateRequest: UpdateAvailabilityRequest = {quantity, initialQuantity, expiryDate};
    const next = (availability: Availability) => {
      this.messageService.showSuccess(`Successfully updated ${availability.medication.name}`);
      this.router.navigate([`/inventory`]);
    };
    const error = () => {
      this.messageService.showError(`Updating ${this.availability!.medication.name} failed due to an unexpected error`);
    };
    this.service
        .update(this.availability!.id, updateRequest)
        .subscribe({next, error});
  }

  cancel(): void {
    this.router.navigate([`/inventory`]);
  }

  private mapParameterId() {
    return (observable: Observable<ParamMap>) => observable.pipe(
      map(parameters => parameters.get('id')),
      filter(id => id != null),
      map(id => id as string)
    );
  }
}
