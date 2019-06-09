/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingExerciseComponent } from 'app/entities/training-exercise/training-exercise.component';
import { TrainingExerciseService } from 'app/entities/training-exercise/training-exercise.service';
import { TrainingExercise } from 'app/shared/model/training-exercise.model';

describe('Component Tests', () => {
  describe('TrainingExercise Management Component', () => {
    let comp: TrainingExerciseComponent;
    let fixture: ComponentFixture<TrainingExerciseComponent>;
    let service: TrainingExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingExerciseComponent],
        providers: []
      })
        .overrideTemplate(TrainingExerciseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrainingExerciseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainingExerciseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TrainingExercise(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.trainingExercises[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
