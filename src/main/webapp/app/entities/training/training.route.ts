import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Training } from 'app/shared/model/training.model';
import { TrainingService } from './training.service';
import { TrainingComponent } from './training.component';
import { TrainingDetailComponent } from './training-detail.component';
import { TrainingUpdateComponent } from './training-update.component';
import { TrainingDeletePopupComponent } from './training-delete-dialog.component';
import { ITraining } from 'app/shared/model/training.model';

@Injectable({ providedIn: 'root' })
export class TrainingResolve implements Resolve<ITraining> {
  constructor(private service: TrainingService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITraining> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Training>) => response.ok),
        map((training: HttpResponse<Training>) => training.body)
      );
    }
    return of(new Training());
  }
}

export const trainingRoute: Routes = [
  {
    path: '',
    component: TrainingComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Trainings'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TrainingDetailComponent,
    resolve: {
      training: TrainingResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Trainings'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TrainingUpdateComponent,
    resolve: {
      training: TrainingResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Trainings'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TrainingUpdateComponent,
    resolve: {
      training: TrainingResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Trainings'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const trainingPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TrainingDeletePopupComponent,
    resolve: {
      training: TrainingResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Trainings'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
