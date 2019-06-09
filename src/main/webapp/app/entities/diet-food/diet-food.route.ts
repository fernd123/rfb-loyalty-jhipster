import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DietFood } from 'app/shared/model/diet-food.model';
import { DietFoodService } from './diet-food.service';
import { DietFoodComponent } from './diet-food.component';
import { DietFoodDetailComponent } from './diet-food-detail.component';
import { DietFoodUpdateComponent } from './diet-food-update.component';
import { DietFoodDeletePopupComponent } from './diet-food-delete-dialog.component';
import { IDietFood } from 'app/shared/model/diet-food.model';

@Injectable({ providedIn: 'root' })
export class DietFoodResolve implements Resolve<IDietFood> {
  constructor(private service: DietFoodService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDietFood> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DietFood>) => response.ok),
        map((dietFood: HttpResponse<DietFood>) => dietFood.body)
      );
    }
    return of(new DietFood());
  }
}

export const dietFoodRoute: Routes = [
  {
    path: '',
    component: DietFoodComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DietFoods'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DietFoodDetailComponent,
    resolve: {
      dietFood: DietFoodResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DietFoods'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DietFoodUpdateComponent,
    resolve: {
      dietFood: DietFoodResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DietFoods'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DietFoodUpdateComponent,
    resolve: {
      dietFood: DietFoodResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DietFoods'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dietFoodPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DietFoodDeletePopupComponent,
    resolve: {
      dietFood: DietFoodResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DietFoods'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
