import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Validators, FormBuilder } from '@angular/forms';
import { CustomerService } from 'app/entities/customer';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICustomer, Customer } from 'app/shared/model/customer.model';
import moment = require('moment');

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer.scss']
})
export class CustomerDetailComponent implements OnInit {
  customer: ICustomer;
  closeResult: string;
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
    protected activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private customerService: CustomerService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
    });
  }

  previousState() {
    window.history.back();
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
    debugger;
    this.isSaving = true;
    if (type == 'status') {
      this.customer.isActive = !this.customer.isActive;
      this.customerService.update(this.customer).subscribe(res => {
        this.customer = res.body;
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

  /* Modal Functions */
  open(content) {
    this.updateForm(this.customer);
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      result => {
        this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>) {
    result.subscribe(
      (res: HttpResponse<ICustomer>) => {
        this.customer = res.body;
        this.onSaveSuccess();
      },
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.modalService.dismissAll();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
