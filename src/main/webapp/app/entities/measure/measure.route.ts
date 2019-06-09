import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Measure } from 'app/shared/model/measure.model';
import { MeasureService } from './measure.service';
import { MeasureComponent } from './measure.component';
import { MeasureDetailComponent } from './measure-detail.component';
import { MeasureUpdateComponent } from './measure-update.component';
import { MeasureDeletePopupComponent } from './measure-delete-dialog.component';
import { IMeasure } from 'app/shared/model/measure.model';

@Injectable({ providedIn: 'root' })
export class MeasureResolve implements Resolve<IMeasure> {
  constructor(private service: MeasureService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMeasure> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Measure>) => response.ok),
        map((measure: HttpResponse<Measure>) => measure.body)
      );
    }
    return of(new Measure());
  }
}

export const measureRoute: Routes = [
  {
    path: '',
    component: MeasureComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Measures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MeasureDetailComponent,
    resolve: {
      measure: MeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Measures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MeasureUpdateComponent,
    resolve: {
      measure: MeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Measures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MeasureUpdateComponent,
    resolve: {
      measure: MeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Measures'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const measurePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MeasureDeletePopupComponent,
    resolve: {
      measure: MeasureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Measures'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
