import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrainingDay } from 'app/shared/model/training-day.model';

@Component({
  selector: 'jhi-training-day-detail',
  templateUrl: './training-day-detail.component.html'
})
export class TrainingDayDetailComponent implements OnInit {
  trainingDay: ITrainingDay;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ trainingDay }) => {
      this.trainingDay = trainingDay;
    });
  }

  previousState() {
    window.history.back();
  }
}
