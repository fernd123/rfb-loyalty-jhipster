import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  DietComponent,
  DietDetailComponent,
  DietUpdateComponent,
  DietDeletePopupComponent,
  DietDeleteDialogComponent,
  dietRoute,
  dietPopupRoute
} from './';

const ENTITY_STATES = [...dietRoute, ...dietPopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [DietComponent, DietDetailComponent, DietUpdateComponent, DietDeleteDialogComponent, DietDeletePopupComponent],
  entryComponents: [DietComponent, DietUpdateComponent, DietDeleteDialogComponent, DietDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyDietModule {}
