import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  MeasureComponent,
  MeasureDetailComponent,
  MeasureUpdateComponent,
  MeasureDeletePopupComponent,
  MeasureDeleteDialogComponent,
  measureRoute,
  measurePopupRoute
} from './';

const ENTITY_STATES = [...measureRoute, ...measurePopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MeasureComponent,
    MeasureDetailComponent,
    MeasureUpdateComponent,
    MeasureDeleteDialogComponent,
    MeasureDeletePopupComponent
  ],
  entryComponents: [MeasureComponent, MeasureUpdateComponent, MeasureDeleteDialogComponent, MeasureDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyMeasureModule {}
