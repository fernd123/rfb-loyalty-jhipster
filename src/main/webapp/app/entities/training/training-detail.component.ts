import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITraining } from 'app/shared/model/training.model';

@Component({
  selector: 'jhi-training-detail',
  templateUrl: './training-detail.component.html'
})
export class TrainingDetailComponent implements OnInit {
  training: ITraining;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ training }) => {
      this.training = training;
    });
  }

  previousState() {
    window.history.back();
  }
}
