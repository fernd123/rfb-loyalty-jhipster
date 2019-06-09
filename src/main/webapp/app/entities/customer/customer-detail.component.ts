import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { CustomerService } from 'app/entities/customer';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomer } from 'app/shared/model/customer.model';

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer.scss']
})
export class CustomerDetailComponent implements OnInit {
  closeResult: string;
  isSaving: boolean;
  customer: ICustomer;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private customerService: CustomerService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      this.customerService.customer = customer;
    });
  }

  previousState() {
    window.history.back();
  }

  save(type: string) {
    this.isSaving = true;
    if (type == 'status') {
      this.customerService.customer.isActive = !this.customerService.customer.isActive;
      this.customerService.update(this.customerService.customer).subscribe(res => {
        this.customerService.customer = res.body;
        this.onSaveSuccess();
      });
    }
  }

  /* Modal Functions */
  open(content) {
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
        this.customerService.customer = res.body;
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
