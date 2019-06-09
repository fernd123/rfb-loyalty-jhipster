import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  CustomerComponent,
  CustomerDetailComponent,
  CustomerUpdateComponent,
  CustomerDeletePopupComponent,
  CustomerDeleteDialogComponent,
  CustomerEditPopupComponent,
  CustomerEditDialogComponent,
  CustomerMeasurePopupComponent,
  CustomerMeasureDialogComponent,
  customerRoute,
  customerPopupRoute,
  customerEditPopupRoute,
  customerMeasurePopupRoute
} from './';

const ENTITY_STATES = [...customerRoute, ...customerPopupRoute, ...customerEditPopupRoute, ...customerMeasurePopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CustomerComponent,
    CustomerDetailComponent,
    CustomerUpdateComponent,
    CustomerDeleteDialogComponent,
    CustomerDeletePopupComponent,
    CustomerEditPopupComponent,
    CustomerEditDialogComponent,
    CustomerMeasurePopupComponent,
    CustomerMeasureDialogComponent
  ],
  entryComponents: [
    CustomerComponent,
    CustomerUpdateComponent,
    CustomerDeleteDialogComponent,
    CustomerDeletePopupComponent,
    CustomerEditPopupComponent,
    CustomerEditDialogComponent,
    CustomerMeasurePopupComponent,
    CustomerMeasureDialogComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyCustomerModule {}
