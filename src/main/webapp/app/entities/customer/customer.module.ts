import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { LOCALE_ID } from '@angular/core';

import { RouterModule } from '@angular/router';
import localeEs from '@angular/common/locales/es';
import { registerLocaleData } from '@angular/common';

import { RfbloyaltySharedModule } from 'app/shared';

registerLocaleData(localeEs, 'es');

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
  CustomerTrainingPopupComponent,
  CustomerTrainingDialogComponent,
  CustomerDietPopupComponent,
  CustomerDietDialogComponent,
  customerRoute,
  customerPopupRoute,
  customerEditPopupRoute,
  customerMeasurePopupRoute,
  customerTrainingPopupRoute,
  customerDietPopupRoute
} from './';

const ENTITY_STATES = [
  ...customerRoute,
  ...customerPopupRoute,
  ...customerEditPopupRoute,
  ...customerMeasurePopupRoute,
  ...customerTrainingPopupRoute,
  ...customerDietPopupRoute
];

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
    CustomerMeasureDialogComponent,
    CustomerTrainingPopupComponent,
    CustomerTrainingDialogComponent,
    CustomerDietPopupComponent,
    CustomerDietDialogComponent
  ],
  providers: [{ provide: LOCALE_ID, useValue: 'es' }],

  entryComponents: [
    CustomerComponent,
    CustomerUpdateComponent,
    CustomerDeleteDialogComponent,
    CustomerDeletePopupComponent,
    CustomerEditPopupComponent,
    CustomerEditDialogComponent,
    CustomerMeasurePopupComponent,
    CustomerMeasureDialogComponent,
    CustomerTrainingPopupComponent,
    CustomerTrainingDialogComponent,
    CustomerDietPopupComponent,
    CustomerDietDialogComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyCustomerModule {}
