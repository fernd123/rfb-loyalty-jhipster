import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICustomerDate } from 'app/shared/model/customer-date.model';
import { AccountService } from 'app/core';
import { CustomerDateService } from './customer-date.service';

@Component({
  selector: 'jhi-customer-date',
  templateUrl: './customer-date.component.html'
})
export class CustomerDateComponent implements OnInit, OnDestroy {
  customerDates: ICustomerDate[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected customerDateService: CustomerDateService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.customerDateService
      .query()
      .pipe(
        filter((res: HttpResponse<ICustomerDate[]>) => res.ok),
        map((res: HttpResponse<ICustomerDate[]>) => res.body)
      )
      .subscribe(
        (res: ICustomerDate[]) => {
          this.customerDates = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCustomerDates();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICustomerDate) {
    return item.id;
  }

  registerChangeInCustomerDates() {
    this.eventSubscriber = this.eventManager.subscribe('customerDateListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
