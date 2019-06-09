/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingExerciseUpdateComponent } from 'app/entities/training-exercise/training-exercise-update.component';
import { TrainingExerciseService } from 'app/entities/training-exercise/training-exercise.service';
import { TrainingExercise } from 'app/shared/model/training-exercise.model';

describe('Component Tests', () => {
  describe('TrainingExercise Management Update Component', () => {
    let comp: TrainingExerciseUpdateComponent;
    let fixture: ComponentFixture<TrainingExerciseUpdateComponent>;
    let service: TrainingExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingExerciseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TrainingExerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrainingExerciseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainingExerciseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TrainingExercise(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TrainingExercise();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
