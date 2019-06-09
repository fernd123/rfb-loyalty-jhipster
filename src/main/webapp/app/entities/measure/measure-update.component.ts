import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IMeasure, Measure } from 'app/shared/model/measure.model';
import { MeasureService } from './measure.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
  selector: 'jhi-measure-update',
  templateUrl: './measure-update.component.html'
})
export class MeasureUpdateComponent implements OnInit {
  measure: IMeasure;
  isSaving: boolean;

  customers: ICustomer[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    arm: [],
    ribCage: [],
    leg: [],
    customer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected measureService: MeasureService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ measure }) => {
      this.updateForm(measure);
      this.measure = measure;
    });
    this.customerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICustomer[]>) => response.body)
      )
      .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(measure: IMeasure) {
    this.editForm.patchValue({
      id: measure.id,
      creationDate: measure.creationDate != null ? measure.creationDate.format(DATE_TIME_FORMAT) : null,
      arm: measure.arm,
      ribCage: measure.ribCage,
      leg: measure.leg,
      customer: measure.customer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const measure = this.createFromForm();
    if (measure.id !== undefined) {
      this.subscribeToSaveResponse(this.measureService.update(measure));
    } else {
      this.subscribeToSaveResponse(this.measureService.create(measure));
    }
  }

  private createFromForm(): IMeasure {
    const entity = {
      ...new Measure(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      arm: this.editForm.get(['arm']).value,
      ribCage: this.editForm.get(['ribCage']).value,
      leg: this.editForm.get(['leg']).value,
      customer: this.editForm.get(['customer']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasure>>) {
    result.subscribe((res: HttpResponse<IMeasure>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
