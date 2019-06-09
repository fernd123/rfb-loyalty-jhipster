import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  TrainingDayComponent,
  TrainingDayDetailComponent,
  TrainingDayUpdateComponent,
  TrainingDayDeletePopupComponent,
  TrainingDayDeleteDialogComponent,
  trainingDayRoute,
  trainingDayPopupRoute
} from './';

const ENTITY_STATES = [...trainingDayRoute, ...trainingDayPopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TrainingDayComponent,
    TrainingDayDetailComponent,
    TrainingDayUpdateComponent,
    TrainingDayDeleteDialogComponent,
    TrainingDayDeletePopupComponent
  ],
  entryComponents: [TrainingDayComponent, TrainingDayUpdateComponent, TrainingDayDeleteDialogComponent, TrainingDayDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyTrainingDayModule {}
