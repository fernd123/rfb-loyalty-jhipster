import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITrainingDay } from 'app/shared/model/training-day.model';
import { AccountService } from 'app/core';
import { TrainingDayService } from './training-day.service';

@Component({
  selector: 'jhi-training-day',
  templateUrl: './training-day.component.html'
})
export class TrainingDayComponent implements OnInit, OnDestroy {
  trainingDays: ITrainingDay[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected trainingDayService: TrainingDayService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.trainingDayService
      .query()
      .pipe(
        filter((res: HttpResponse<ITrainingDay[]>) => res.ok),
        map((res: HttpResponse<ITrainingDay[]>) => res.body)
      )
      .subscribe(
        (res: ITrainingDay[]) => {
          this.trainingDays = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTrainingDays();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITrainingDay) {
    return item.id;
  }

  registerChangeInTrainingDays() {
    this.eventSubscriber = this.eventManager.subscribe('trainingDayListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
