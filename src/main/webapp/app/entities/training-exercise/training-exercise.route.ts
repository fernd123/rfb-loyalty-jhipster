import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TrainingExercise } from 'app/shared/model/training-exercise.model';
import { TrainingExerciseService } from './training-exercise.service';
import { TrainingExerciseComponent } from './training-exercise.component';
import { TrainingExerciseDetailComponent } from './training-exercise-detail.component';
import { TrainingExerciseUpdateComponent } from './training-exercise-update.component';
import { TrainingExerciseDeletePopupComponent } from './training-exercise-delete-dialog.component';
import { ITrainingExercise } from 'app/shared/model/training-exercise.model';

@Injectable({ providedIn: 'root' })
export class TrainingExerciseResolve implements Resolve<ITrainingExercise> {
  constructor(private service: TrainingExerciseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITrainingExercise> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TrainingExercise>) => response.ok),
        map((trainingExercise: HttpResponse<TrainingExercise>) => trainingExercise.body)
      );
    }
    return of(new TrainingExercise());
  }
}

export const trainingExerciseRoute: Routes = [
  {
    path: '',
    component: TrainingExerciseComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingExercises'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TrainingExerciseDetailComponent,
    resolve: {
      trainingExercise: TrainingExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingExercises'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TrainingExerciseUpdateComponent,
    resolve: {
      trainingExercise: TrainingExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingExercises'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TrainingExerciseUpdateComponent,
    resolve: {
      trainingExercise: TrainingExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingExercises'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const trainingExercisePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TrainingExerciseDeletePopupComponent,
    resolve: {
      trainingExercise: TrainingExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TrainingExercises'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
