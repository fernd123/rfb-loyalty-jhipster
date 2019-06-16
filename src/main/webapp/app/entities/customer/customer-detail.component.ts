import { MeasureService } from './../measure/measure.service';
import { Observable, forkJoin } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { CustomerService } from 'app/entities/customer';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomer } from 'app/shared/model/customer.model';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer.scss']
})
export class CustomerDetailComponent implements OnInit {
  closeResult: string;
  isSaving: boolean;
  customer: ICustomer;
  links: any;
  page: any;
  itemsPerPage: number;
  predicate: any;
  reverse: any;
  totalItems: number;
  @ViewChild('customerValues') customerValuesList;

  showMeasure: boolean = true;
  showData: boolean = false;
  customerInfoList: any = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private customerService: CustomerService,
    private measureService: MeasureService,
    private fb: FormBuilder
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  reset() {
    this.page = 0;
  }

  loadPage(page) {
    this.page = page;
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      this.customerService.customer = customer;
      this.customerService.refreshAll();
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

  showInfo(type: string, element: any) {
    /** Set active li */
    const childElements = this.customerValuesList.nativeElement.children;
    if (type == 'measure') {
      this.showMeasure = true;
      this.showData = false;
    } else {
      if (type == 'diet') {
        this.customerInfoList = this.customerService.customer.customerDiets;
      } else if (type == 'training') {
        this.customerInfoList = this.customerService.customer.customerTrainings;
      } else {
        this.customerInfoList = this.customerService.customer.customerDates;
      }
      this.showData = true;
      this.showMeasure = false;
    }

    for (const childLi of childElements) {
      childLi.children[0].className = 'list-group-item';
    }
    element.className = 'list-group-item active';
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
