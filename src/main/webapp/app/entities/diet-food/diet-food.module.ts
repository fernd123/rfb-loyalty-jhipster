import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  DietFoodComponent,
  DietFoodDetailComponent,
  DietFoodUpdateComponent,
  DietFoodDeletePopupComponent,
  DietFoodDeleteDialogComponent,
  dietFoodRoute,
  dietFoodPopupRoute
} from './';

const ENTITY_STATES = [...dietFoodRoute, ...dietFoodPopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DietFoodComponent,
    DietFoodDetailComponent,
    DietFoodUpdateComponent,
    DietFoodDeleteDialogComponent,
    DietFoodDeletePopupComponent
  ],
  entryComponents: [DietFoodComponent, DietFoodUpdateComponent, DietFoodDeleteDialogComponent, DietFoodDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyDietFoodModule {}
