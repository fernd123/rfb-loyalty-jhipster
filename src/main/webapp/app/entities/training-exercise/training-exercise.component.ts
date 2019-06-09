import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITrainingExercise } from 'app/shared/model/training-exercise.model';
import { AccountService } from 'app/core';
import { TrainingExerciseService } from './training-exercise.service';

@Component({
  selector: 'jhi-training-exercise',
  templateUrl: './training-exercise.component.html'
})
export class TrainingExerciseComponent implements OnInit, OnDestroy {
  trainingExercises: ITrainingExercise[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected trainingExerciseService: TrainingExerciseService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.trainingExerciseService
      .query()
      .pipe(
        filter((res: HttpResponse<ITrainingExercise[]>) => res.ok),
        map((res: HttpResponse<ITrainingExercise[]>) => res.body)
      )
      .subscribe(
        (res: ITrainingExercise[]) => {
          this.trainingExercises = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTrainingExercises();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITrainingExercise) {
    return item.id;
  }

  registerChangeInTrainingExercises() {
    this.eventSubscriber = this.eventManager.subscribe('trainingExerciseListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
