import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html'
})
export class CustomerUpdateComponent implements OnInit {
  customer: ICustomer;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    firstName: [null, [Validators.required]],
    birthDate: [null, [Validators.required]],
    sex: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    email: [],
    objective: [],
    observations: [],
    creationDate: [],
    isActive: []
  });

  constructor(protected customerService: CustomerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.updateForm(customer);
      this.customer = customer;
    });
  }

  updateForm(customer: ICustomer) {
    this.editForm.patchValue({
      id: customer.id,
      name: customer.name,
      firstName: customer.firstName,
      birthDate: customer.birthDate != null ? customer.birthDate.format(DATE_TIME_FORMAT) : null,
      sex: customer.sex,
      phone: customer.phone,
      email: customer.email,
      objective: customer.objective,
      observations: customer.observations,
      creationDate: customer.creationDate != null ? customer.creationDate.format(DATE_TIME_FORMAT) : null,
      isActive: customer.isActive
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  private createFromForm(): ICustomer {
    const entity = {
      ...new Customer(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      firstName: this.editForm.get(['firstName']).value,
      birthDate:
        this.editForm.get(['birthDate']).value != null ? moment(this.editForm.get(['birthDate']).value, DATE_TIME_FORMAT) : undefined,
      sex: this.editForm.get(['sex']).value,
      phone: this.editForm.get(['phone']).value,
      email: this.editForm.get(['email']).value,
      objective: this.editForm.get(['objective']).value,
      observations: this.editForm.get(['observations']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      isActive: this.editForm.get(['isActive']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>) {
    result.subscribe((res: HttpResponse<ICustomer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
