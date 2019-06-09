import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  CustomerDateComponent,
  CustomerDateDetailComponent,
  CustomerDateUpdateComponent,
  CustomerDateDeletePopupComponent,
  CustomerDateDeleteDialogComponent,
  customerDateRoute,
  customerDatePopupRoute
} from './';

const ENTITY_STATES = [...customerDateRoute, ...customerDatePopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CustomerDateComponent,
    CustomerDateDetailComponent,
    CustomerDateUpdateComponent,
    CustomerDateDeleteDialogComponent,
    CustomerDateDeletePopupComponent
  ],
  entryComponents: [
    CustomerDateComponent,
    CustomerDateUpdateComponent,
    CustomerDateDeleteDialogComponent,
    CustomerDateDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyCustomerDateModule {}
