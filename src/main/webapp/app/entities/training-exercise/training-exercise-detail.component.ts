import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrainingExercise } from 'app/shared/model/training-exercise.model';

@Component({
  selector: 'jhi-training-exercise-detail',
  templateUrl: './training-exercise-detail.component.html'
})
export class TrainingExerciseDetailComponent implements OnInit {
  trainingExercise: ITrainingExercise;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ trainingExercise }) => {
      this.trainingExercise = trainingExercise;
    });
  }

  previousState() {
    window.history.back();
  }
}
