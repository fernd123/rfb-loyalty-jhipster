import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITrainingExercise, TrainingExercise } from 'app/shared/model/training-exercise.model';
import { TrainingExerciseService } from './training-exercise.service';
import { ITrainingDay } from 'app/shared/model/training-day.model';
import { TrainingDayService } from 'app/entities/training-day';
import { IExercise } from 'app/shared/model/exercise.model';
import { ExerciseService } from 'app/entities/exercise';

@Component({
  selector: 'jhi-training-exercise-update',
  templateUrl: './training-exercise-update.component.html'
})
export class TrainingExerciseUpdateComponent implements OnInit {
  trainingExercise: ITrainingExercise;
  isSaving: boolean;

  trainingdays: ITrainingDay[];

  exercises: IExercise[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    trainingNumber: [null, [Validators.required]],
    description: [],
    trainingDay: [],
    exercise: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected trainingExerciseService: TrainingExerciseService,
    protected trainingDayService: TrainingDayService,
    protected exerciseService: ExerciseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ trainingExercise }) => {
      this.updateForm(trainingExercise);
      this.trainingExercise = trainingExercise;
    });
    this.trainingDayService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITrainingDay[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITrainingDay[]>) => response.body)
      )
      .subscribe((res: ITrainingDay[]) => (this.trainingdays = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.exerciseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercise[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercise[]>) => response.body)
      )
      .subscribe((res: IExercise[]) => (this.exercises = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(trainingExercise: ITrainingExercise) {
    this.editForm.patchValue({
      id: trainingExercise.id,
      creationDate: trainingExercise.creationDate != null ? trainingExercise.creationDate.format(DATE_TIME_FORMAT) : null,
      trainingNumber: trainingExercise.trainingNumber,
      description: trainingExercise.description,
      trainingDay: trainingExercise.trainingDay,
      exercise: trainingExercise.exercise
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const trainingExercise = this.createFromForm();
    if (trainingExercise.id !== undefined) {
      this.subscribeToSaveResponse(this.trainingExerciseService.update(trainingExercise));
    } else {
      this.subscribeToSaveResponse(this.trainingExerciseService.create(trainingExercise));
    }
  }

  private createFromForm(): ITrainingExercise {
    const entity = {
      ...new TrainingExercise(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      trainingNumber: this.editForm.get(['trainingNumber']).value,
      description: this.editForm.get(['description']).value,
      trainingDay: this.editForm.get(['trainingDay']).value,
      exercise: this.editForm.get(['exercise']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingExercise>>) {
    result.subscribe((res: HttpResponse<ITrainingExercise>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackTrainingDayById(index: number, item: ITrainingDay) {
    return item.id;
  }

  trackExerciseById(index: number, item: IExercise) {
    return item.id;
  }
}
