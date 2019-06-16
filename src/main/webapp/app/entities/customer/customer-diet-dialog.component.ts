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
    items: this.fb.array([])
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
    debugger;
    this.customerService.refreshAll();
  }

  addDietFood() {
    this.items = this.editForm.get('items') as FormArray;
    this.items.push(this.createItem());
  }

  removeDietDay(i: number) {
    console.log(i);
    this.items = this.editForm.get('items') as FormArray;
    this.items.controls.splice(i, 1);
  }

  createItem(description?: string): FormGroup {
    return this.fb.group({
      description: description
    });
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  updateForm() {
    this.editForm.patchValue({
      id: this.dietService.dietSelected != null ? this.dietService.dietSelected.id : undefined,
      creationDate:
        this.dietService.dietSelected != null && this.dietService.dietSelected.creationDate != null
          ? this.dietService.dietSelected.creationDate.format(DATE_TIME_FORMAT)
          : moment().format(DATE_TIME_FORMAT),
      name: this.dietService.dietSelected != null ? this.dietService.dietSelected.name : undefined,
      customer: this.customerService.customer
    });

    this.items = this.editForm.get('items') as FormArray;
    if (this.dietService.dietSelected == null) {
      this.items.push(this.createItem());
    }

    if (this.dietService.dietSelected != null) {
      if (this.dietService.dietSelected.food1 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food1));
      }

      if (this.dietService.dietSelected.food2 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food2));
      }

      if (this.dietService.dietSelected.food3 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food3));
      }

      if (this.dietService.dietSelected.food4 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food4));
      }

      if (this.dietService.dietSelected.food5 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food5));
      }

      if (this.dietService.dietSelected.food6 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food6));
      }

      if (this.dietService.dietSelected.food7 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food7));
      }

      if (this.dietService.dietSelected.food8 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food8));
      }

      if (this.dietService.dietSelected.food9 != null) {
        this.items.push(this.createItem(this.dietService.dietSelected.food9));
      }
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    debugger;
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
      food1: this.editForm.get('items').value[0] != undefined ? this.editForm.get('items').value[0].description : null,
      food2: this.editForm.get('items').value[1] != undefined ? this.editForm.get('items').value[1].description : null,
      food3: this.editForm.get('items').value[2] != undefined ? this.editForm.get('items').value[2].description : null,
      food4: this.editForm.get('items').value[3] != undefined ? this.editForm.get('items').value[3].description : null,
      food5: this.editForm.get('items').value[4] != undefined ? this.editForm.get('items').value[4].description : null,
      food6: this.editForm.get('items').value[5] != undefined ? this.editForm.get('items').value[5].description : null,
      food7: this.editForm.get('items').value[6] != undefined ? this.editForm.get('items').value[6].description : null,
      food8: this.editForm.get('items').value[7] != undefined ? this.editForm.get('items').value[7].description : null,
      food9: this.editForm.get('items').value[8] != undefined ? this.editForm.get('items').value[8].description : null,

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
