import { IDietFood } from './../../shared/model/diet-food.model';
import { DietFoodService } from './../diet-food/diet-food.service';
import { DietFood } from 'app/shared/model/diet-food.model';
import { filter, map } from 'rxjs/operators';
import { DietService } from 'app/entities/diet';
import { Diet } from './../../shared/model/diet.model';
import { IDiet } from 'app/shared/model/diet.model';
import { NgbModal, NgbModalRef, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IMeasure, Measure } from 'app/shared/model/measure.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
  selector: 'jhi-customer-diet-dialog',
  templateUrl: './customer-diet-dialog.component.html'
})
export class CustomerDietDialogComponent {
  diet: IDiet;
  isSaving: boolean;
  items: FormArray;

  customers: ICustomer[];
  customer: ICustomer;

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    name: [null, [Validators.required]],
    items: this.fb.array([this.createItem()])
  });

  constructor(
    public activeModal: NgbActiveModal,
    protected jhiAlertService: JhiAlertService,
    protected dietService: DietService,
    protected dietFoodService: DietFoodService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.updateForm();
  }

  ngOnDestroy() {
    this.customerService.refreshAll();
  }

  addDietDay() {
    this.items = this.editForm.get('items') as FormArray;
    this.items.push(this.createItem());
  }

  removeDietDay(i: number) {
    console.log(i);
    this.items = this.editForm.get('items') as FormArray;
    this.items.controls.splice(i, 1);
  }

  createItem(): FormGroup {
    return this.fb.group({
      description: ''
    });
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  updateForm() {
    this.editForm.patchValue({
      id: this.dietService.dietSelected != undefined ? this.dietService.dietSelected.id : undefined,
      creationDate:
        this.dietService.dietSelected != undefined && this.dietService.dietSelected.creationDate != null
          ? this.dietService.dietSelected.creationDate.format(DATE_TIME_FORMAT)
          : moment().format(DATE_TIME_FORMAT),
      name: this.dietService.dietSelected != undefined ? this.dietService.dietSelected.name : undefined,
      customer: this.customerService.customer
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
      //id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name']).value,
      customer: this.customer
    };

    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiet>>) {
    result.subscribe(
      (res: HttpResponse<IDiet>) => {
        this.diet = res.body;
        this.onSaveSuccess();
      },
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  protected subscribeToSaveDietFoodResponse(result: Observable<HttpResponse<IDietFood>>) {
    result.subscribe((res: HttpResponse<IDietFood>) => {}, (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    /* Save the Diet Days */
    let itemsArray = this.editForm.get('items').value;
    for (let i in itemsArray) {
      let dietFood: DietFood = new DietFood(null, null, itemsArray[i].description, this.diet);
      this.subscribeToSaveDietFoodResponse(this.dietFoodService.create(dietFood));
    }
    this.clear();
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
  selector: 'jhi-customer-diet-popup',
  template: ''
})
export class CustomerDietPopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerDietDialogComponent as Component, { size: 'lg', backdrop: 'static' });
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
