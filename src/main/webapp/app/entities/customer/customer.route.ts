import { DietService } from './../diet/diet.service';
import { Measure } from 'app/shared/model/measure.model';
import { CustomerDietPopupComponent } from './customer-diet-dialog.component';
import { CustomerTrainingPopupComponent } from './customer-training-dialog.component';
import { MeasureService } from 'app/entities/measure/measure.service';
import { CustomerMeasurePopupComponent } from './customer-measure-dialog.component';
import { CustomerEditPopupComponent } from './customer-edit-dialog.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of, forkJoin } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import { CustomerComponent } from './customer.component';
import { CustomerDetailComponent } from './customer-detail.component';
import { CustomerUpdateComponent } from './customer-update.component';
import { CustomerDeletePopupComponent } from './customer-delete-dialog.component';
import { ICustomer } from 'app/shared/model/customer.model';

@Injectable({ providedIn: 'root' })
export class CustomerResolve implements Resolve<ICustomer> {
  constructor(private customerService: CustomerService, private measureService: MeasureService, private dietService: DietService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomer> {
    const id = route.params['id'] ? route.params['id'] : null;
    const measureId = route.params['measureId'] ? route.params['measureId'] : null;
    const dietId = route.params['dietId'] ? route.params['dietId'] : null;
    debugger;
    if (measureId == 'null' || measureId == null) {
      this.measureService.measureSelected = null;
    } else {
      forkJoin([this.measureService.find(measureId)]).subscribe(results => {
        this.measureService.measureSelected = results[0].body;
      });
    }

    if (dietId == 'null' || dietId == null) {
      this.dietService.dietSelected = null;
    } else {
      forkJoin([this.dietService.find(dietId)]).subscribe(results => {
        this.dietService.dietSelected = results[0].body;
        debugger;
      });
    }

    if (id) {
      return this.customerService.find(id).pipe(
        filter((response: HttpResponse<Customer>) => response.ok),
        map((customer: HttpResponse<Customer>) => customer.body)
      );
    }

    return of(new Customer());
  }
}

export const customerRoute: Routes = [
  {
    path: '',
    component: CustomerComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CustomerDetailComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CustomerUpdateComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CustomerUpdateComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const customerPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CustomerDeletePopupComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

/* Edit Customer Info */
export const customerEditPopupRoute: Routes = [
  {
    path: ':id/editInfo',
    component: CustomerEditPopupComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

/* Register Customer Measure */
export const customerMeasurePopupRoute: Routes = [
  {
    path: ':id/customerMeasure/:measureId',
    component: CustomerMeasurePopupComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

/* Register Training Measure */
export const customerTrainingPopupRoute: Routes = [
  {
    path: ':id/customerTraining',
    component: CustomerTrainingPopupComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

/* Register Diet Measure */
export const customerDietPopupRoute: Routes = [
  {
    path: ':id/customerDiet/:dietId',
    component: CustomerDietPopupComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
