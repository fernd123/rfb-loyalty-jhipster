import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICustomerDate, CustomerDate } from 'app/shared/model/customer-date.model';
import { CustomerDateService } from './customer-date.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
  selector: 'jhi-customer-date-update',
  templateUrl: './customer-date-update.component.html'
})
export class CustomerDateUpdateComponent implements OnInit {
  customerDate: ICustomerDate;
  isSaving: boolean;

  customers: ICustomer[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    observations: [],
    isActive: [],
    customer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected customerDateService: CustomerDateService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ customerDate }) => {
      this.updateForm(customerDate);
      this.customerDate = customerDate;
    });
    this.customerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICustomer[]>) => response.body)
      )
      .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(customerDate: ICustomerDate) {
    this.editForm.patchValue({
      id: customerDate.id,
      creationDate: customerDate.creationDate != null ? customerDate.creationDate.format(DATE_TIME_FORMAT) : null,
      observations: customerDate.observations,
      isActive: customerDate.isActive,
      customer: customerDate.customer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const customerDate = this.createFromForm();
    if (customerDate.id !== undefined) {
      this.subscribeToSaveResponse(this.customerDateService.update(customerDate));
    } else {
      this.subscribeToSaveResponse(this.customerDateService.create(customerDate));
    }
  }

  private createFromForm(): ICustomerDate {
    const entity = {
      ...new CustomerDate(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      observations: this.editForm.get(['observations']).value,
      isActive: this.editForm.get(['isActive']).value,
      customer: this.editForm.get(['customer']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerDate>>) {
    result.subscribe((res: HttpResponse<ICustomerDate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
