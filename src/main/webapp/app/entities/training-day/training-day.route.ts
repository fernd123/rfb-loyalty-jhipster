import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TrainingDay } from 'app/shared/model/training-day.model';
import { TrainingDayService } from './training-day.service';
import { TrainingDayComponent } from './training-day.component';
import { TrainingDayDetailComponent } from './training-day-detail.component';
import { TrainingDayUpdateComponent } from './training-day-update.component';
import { TrainingDayDeletePopupComponent } from './training-day-delete-dialog.component';
import { ITrainingDay } from 'app/shared/model/training-day.model';

@Injectable({ providedIn: 'root' })
export class TrainingDayResolve implements Resolve<ITrainingDay> {
  constructor(private service: TrainingDayService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITrainingDay> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TrainingDay>) => response.ok),
        map((trainingDay: HttpResponse<TrainingDay>) => trainingDay.body)
      );
    }
    return of(new TrainingDay());
  }
}

export const trainingDayRoute: Routes = [
  {
    path: '',
    component: TrainingDayComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingDays'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TrainingDayDetailComponent,
    resolve: {
      trainingDay: TrainingDayResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingDays'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TrainingDayUpdateComponent,
    resolve: {
      trainingDay: TrainingDayResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingDays'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TrainingDayUpdateComponent,
    resolve: {
      trainingDay: TrainingDayResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingDays'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const trainingDayPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TrainingDayDeletePopupComponent,
    resolve: {
      trainingDay: TrainingDayResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingDays'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
