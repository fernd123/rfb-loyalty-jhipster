import { MeasureService } from 'app/entities/measure/measure.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IMeasure, Measure } from 'app/shared/model/measure.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
  selector: 'jhi-customer-measure-dialog',
  templateUrl: './customer-measure-dialog.component.html'
})
export class CustomerMeasureDialogComponent {
  customer: ICustomer;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    arm: [],
    ribCage: [],
    leg: [],
    customer: []
  });

  constructor(
    public activeModal: NgbActiveModal,
    protected jhiAlertService: JhiAlertService,
    protected measureService: MeasureService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    debugger;
    this.updateForm(this.customer);
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  updateForm(measure: IMeasure) {
    this.editForm.patchValue({
      id: null,
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

@Component({
  selector: 'jhi-customer-measure-popup',
  template: ''
})
export class CustomerMeasurePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerMeasureDialogComponent as Component, { size: 'lg', backdrop: 'static' });
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
