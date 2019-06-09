/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingExerciseDetailComponent } from 'app/entities/training-exercise/training-exercise-detail.component';
import { TrainingExercise } from 'app/shared/model/training-exercise.model';

describe('Component Tests', () => {
  describe('TrainingExercise Management Detail Component', () => {
    let comp: TrainingExerciseDetailComponent;
    let fixture: ComponentFixture<TrainingExerciseDetailComponent>;
    const route = ({ data: of({ trainingExercise: new TrainingExercise(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingExerciseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TrainingExerciseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrainingExerciseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.trainingExercise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
