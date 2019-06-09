import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IExercise, Exercise } from 'app/shared/model/exercise.model';
import { ExerciseService } from './exercise.service';

@Component({
  selector: 'jhi-exercise-update',
  templateUrl: './exercise-update.component.html'
})
export class ExerciseUpdateComponent implements OnInit {
  exercise: IExercise;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    name: [null, [Validators.required]],
    description: [],
    url: []
  });

  constructor(protected exerciseService: ExerciseService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exercise }) => {
      this.updateForm(exercise);
      this.exercise = exercise;
    });
  }

  updateForm(exercise: IExercise) {
    this.editForm.patchValue({
      id: exercise.id,
      creationDate: exercise.creationDate != null ? exercise.creationDate.format(DATE_TIME_FORMAT) : null,
      name: exercise.name,
      description: exercise.description,
      url: exercise.url
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exercise = this.createFromForm();
    if (exercise.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseService.update(exercise));
    } else {
      this.subscribeToSaveResponse(this.exerciseService.create(exercise));
    }
  }

  private createFromForm(): IExercise {
    const entity = {
      ...new Exercise(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      url: this.editForm.get(['url']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercise>>) {
    result.subscribe((res: HttpResponse<IExercise>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
