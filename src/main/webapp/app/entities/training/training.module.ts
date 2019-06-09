import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  TrainingComponent,
  TrainingDetailComponent,
  TrainingUpdateComponent,
  TrainingDeletePopupComponent,
  TrainingDeleteDialogComponent,
  trainingRoute,
  trainingPopupRoute
} from './';

const ENTITY_STATES = [...trainingRoute, ...trainingPopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TrainingComponent,
    TrainingDetailComponent,
    TrainingUpdateComponent,
    TrainingDeleteDialogComponent,
    TrainingDeletePopupComponent
  ],
  entryComponents: [TrainingComponent, TrainingUpdateComponent, TrainingDeleteDialogComponent, TrainingDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyTrainingModule {}
