import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDietFood } from 'app/shared/model/diet-food.model';
import { AccountService } from 'app/core';
import { DietFoodService } from './diet-food.service';

@Component({
  selector: 'jhi-diet-food',
  templateUrl: './diet-food.component.html'
})
export class DietFoodComponent implements OnInit, OnDestroy {
  dietFoods: IDietFood[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected dietFoodService: DietFoodService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.dietFoodService
      .query()
      .pipe(
        filter((res: HttpResponse<IDietFood[]>) => res.ok),
        map((res: HttpResponse<IDietFood[]>) => res.body)
      )
      .subscribe(
        (res: IDietFood[]) => {
          this.dietFoods = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDietFoods();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDietFood) {
    return item.id;
  }

  registerChangeInDietFoods() {
    this.eventSubscriber = this.eventManager.subscribe('dietFoodListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
