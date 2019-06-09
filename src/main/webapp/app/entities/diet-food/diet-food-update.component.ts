import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDietFood, DietFood } from 'app/shared/model/diet-food.model';
import { DietFoodService } from './diet-food.service';
import { IDiet } from 'app/shared/model/diet.model';
import { DietService } from 'app/entities/diet';

@Component({
  selector: 'jhi-diet-food-update',
  templateUrl: './diet-food-update.component.html'
})
export class DietFoodUpdateComponent implements OnInit {
  dietFood: IDietFood;
  isSaving: boolean;

  diets: IDiet[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    description: [null, [Validators.required]],
    diet: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected dietFoodService: DietFoodService,
    protected dietService: DietService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dietFood }) => {
      this.updateForm(dietFood);
      this.dietFood = dietFood;
    });
    this.dietService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDiet[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDiet[]>) => response.body)
      )
      .subscribe((res: IDiet[]) => (this.diets = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(dietFood: IDietFood) {
    this.editForm.patchValue({
      id: dietFood.id,
      creationDate: dietFood.creationDate != null ? dietFood.creationDate.format(DATE_TIME_FORMAT) : null,
      description: dietFood.description,
      diet: dietFood.diet
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const dietFood = this.createFromForm();
    if (dietFood.id !== undefined) {
      this.subscribeToSaveResponse(this.dietFoodService.update(dietFood));
    } else {
      this.subscribeToSaveResponse(this.dietFoodService.create(dietFood));
    }
  }

  private createFromForm(): IDietFood {
    const entity = {
      ...new DietFood(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description']).value,
      diet: this.editForm.get(['diet']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDietFood>>) {
    result.subscribe((res: HttpResponse<IDietFood>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackDietById(index: number, item: IDiet) {
    return item.id;
  }
}
