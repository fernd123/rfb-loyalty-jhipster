import { Customer } from './../../shared/model/customer.model';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { Validators, FormBuilder } from '@angular/forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import moment = require('moment');

@Component({
  selector: 'jhi-customer-edit-dialog',
  templateUrl: './customer-edit-dialog.component.html'
})
export class CustomerEditDialogComponent {
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

  constructor(
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private customerService: CustomerService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.updateForm(this.customer);
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  /* Customer Functions */
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

  save(type: string) {
    this.isSaving = true;
    if (type == 'status') {
      this.customer.isActive = !this.customer.isActive;
      this.customerService.update(this.customer).subscribe(res => {
        this.customer = res.body;
        this.customerService.customer = this.customer;
        this.onSaveSuccess();
      });
    } else {
      const customer = this.createFromForm();
      if (customer.id !== undefined) {
        this.subscribeToSaveResponse(this.customerService.update(customer));
      }
    }
  }

  private createFromForm(): ICustomer {
    const entity = {
      ...new Customer(),
      id: this.customer.id,
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
    result.subscribe(
      (res: HttpResponse<ICustomer>) => {
        this.customerService.customer = res.body;
        this.onSaveSuccess();
      },
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.activeModal.dismiss('cancel');
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

@Component({
  selector: 'jhi-customer-edit-popup',
  template: ''
})
export class CustomerEditPopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerEditDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.customer = customer;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/customer', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/customer', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
