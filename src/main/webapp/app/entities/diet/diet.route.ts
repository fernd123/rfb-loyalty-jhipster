import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Diet } from 'app/shared/model/diet.model';
import { DietService } from './diet.service';
import { DietComponent } from './diet.component';
import { DietDetailComponent } from './diet-detail.component';
import { DietUpdateComponent } from './diet-update.component';
import { DietDeletePopupComponent } from './diet-delete-dialog.component';
import { IDiet } from 'app/shared/model/diet.model';

@Injectable({ providedIn: 'root' })
export class DietResolve implements Resolve<IDiet> {
  constructor(private service: DietService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDiet> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Diet>) => response.ok),
        map((diet: HttpResponse<Diet>) => diet.body)
      );
    }
    return of(new Diet());
  }
}

export const dietRoute: Routes = [
  {
    path: '',
    component: DietComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Diets'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DietDetailComponent,
    resolve: {
      diet: DietResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Diets'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DietUpdateComponent,
    resolve: {
      diet: DietResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Diets'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DietUpdateComponent,
    resolve: {
      diet: DietResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Diets'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dietPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DietDeletePopupComponent,
    resolve: {
      diet: DietResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Diets'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
