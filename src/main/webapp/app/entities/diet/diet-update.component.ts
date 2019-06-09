import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDiet, Diet } from 'app/shared/model/diet.model';
import { DietService } from './diet.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
  selector: 'jhi-diet-update',
  templateUrl: './diet-update.component.html'
})
export class DietUpdateComponent implements OnInit {
  diet: IDiet;
  isSaving: boolean;

  customers: ICustomer[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    name: [null, [Validators.required]],
    customer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected dietService: DietService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ diet }) => {
      this.updateForm(diet);
      this.diet = diet;
    });
    this.customerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICustomer[]>) => response.body)
      )
      .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(diet: IDiet) {
    this.editForm.patchValue({
      id: diet.id,
      creationDate: diet.creationDate != null ? diet.creationDate.format(DATE_TIME_FORMAT) : null,
      name: diet.name,
      customer: diet.customer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const diet = this.createFromForm();
    if (diet.id !== undefined) {
      this.subscribeToSaveResponse(this.dietService.update(diet));
    } else {
      this.subscribeToSaveResponse(this.dietService.create(diet));
    }
  }

  private createFromForm(): IDiet {
    const entity = {
      ...new Diet(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name']).value,
      customer: this.editForm.get(['customer']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiet>>) {
    result.subscribe((res: HttpResponse<IDiet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackCustomerById(index: number, item: ICustomer) {
    return item.id;
  }
}
