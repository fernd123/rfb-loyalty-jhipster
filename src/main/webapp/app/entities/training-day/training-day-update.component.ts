import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITrainingDay, TrainingDay } from 'app/shared/model/training-day.model';
import { TrainingDayService } from './training-day.service';
import { ITraining } from 'app/shared/model/training.model';
import { TrainingService } from 'app/entities/training';

@Component({
  selector: 'jhi-training-day-update',
  templateUrl: './training-day-update.component.html'
})
export class TrainingDayUpdateComponent implements OnInit {
  trainingDay: ITrainingDay;
  isSaving: boolean;

  trainings: ITraining[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    name: [null, [Validators.required]],
    training: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected trainingDayService: TrainingDayService,
    protected trainingService: TrainingService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ trainingDay }) => {
      this.updateForm(trainingDay);
      this.trainingDay = trainingDay;
    });
    this.trainingService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITraining[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITraining[]>) => response.body)
      )
      .subscribe((res: ITraining[]) => (this.trainings = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(trainingDay: ITrainingDay) {
    this.editForm.patchValue({
      id: trainingDay.id,
      creationDate: trainingDay.creationDate != null ? trainingDay.creationDate.format(DATE_TIME_FORMAT) : null,
      name: trainingDay.name,
      training: trainingDay.training
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const trainingDay = this.createFromForm();
    if (trainingDay.id !== undefined) {
      this.subscribeToSaveResponse(this.trainingDayService.update(trainingDay));
    } else {
      this.subscribeToSaveResponse(this.trainingDayService.create(trainingDay));
    }
  }

  private createFromForm(): ITrainingDay {
    const entity = {
      ...new TrainingDay(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name']).value,
      training: this.editForm.get(['training']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingDay>>) {
    result.subscribe((res: HttpResponse<ITrainingDay>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackTrainingById(index: number, item: ITraining) {
    return item.id;
  }
}
