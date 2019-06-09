import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerDate } from 'app/shared/model/customer-date.model';
import { CustomerDateService } from './customer-date.service';
import { CustomerDateComponent } from './customer-date.component';
import { CustomerDateDetailComponent } from './customer-date-detail.component';
import { CustomerDateUpdateComponent } from './customer-date-update.component';
import { CustomerDateDeletePopupComponent } from './customer-date-delete-dialog.component';
import { ICustomerDate } from 'app/shared/model/customer-date.model';

@Injectable({ providedIn: 'root' })
export class CustomerDateResolve implements Resolve<ICustomerDate> {
  constructor(private service: CustomerDateService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomerDate> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CustomerDate>) => response.ok),
        map((customerDate: HttpResponse<CustomerDate>) => customerDate.body)
      );
    }
    return of(new CustomerDate());
  }
}

export const customerDateRoute: Routes = [
  {
    path: '',
    component: CustomerDateComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerDates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CustomerDateDetailComponent,
    resolve: {
      customerDate: CustomerDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerDates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CustomerDateUpdateComponent,
    resolve: {
      customerDate: CustomerDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerDates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CustomerDateUpdateComponent,
    resolve: {
      customerDate: CustomerDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerDates'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const customerDatePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CustomerDateDeletePopupComponent,
    resolve: {
      customerDate: CustomerDateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CustomerDates'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
