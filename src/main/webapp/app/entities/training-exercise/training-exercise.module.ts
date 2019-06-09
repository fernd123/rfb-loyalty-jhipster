import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RfbloyaltySharedModule } from 'app/shared';
import {
  TrainingExerciseComponent,
  TrainingExerciseDetailComponent,
  TrainingExerciseUpdateComponent,
  TrainingExerciseDeletePopupComponent,
  TrainingExerciseDeleteDialogComponent,
  trainingExerciseRoute,
  trainingExercisePopupRoute
} from './';

const ENTITY_STATES = [...trainingExerciseRoute, ...trainingExercisePopupRoute];

@NgModule({
  imports: [RfbloyaltySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TrainingExerciseComponent,
    TrainingExerciseDetailComponent,
    TrainingExerciseUpdateComponent,
    TrainingExerciseDeleteDialogComponent,
    TrainingExerciseDeletePopupComponent
  ],
  entryComponents: [
    TrainingExerciseComponent,
    TrainingExerciseUpdateComponent,
    TrainingExerciseDeleteDialogComponent,
    TrainingExerciseDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyTrainingExerciseModule {}
