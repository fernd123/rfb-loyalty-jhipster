/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingDayUpdateComponent } from 'app/entities/training-day/training-day-update.component';
import { TrainingDayService } from 'app/entities/training-day/training-day.service';
import { TrainingDay } from 'app/shared/model/training-day.model';

describe('Component Tests', () => {
  describe('TrainingDay Management Update Component', () => {
    let comp: TrainingDayUpdateComponent;
    let fixture: ComponentFixture<TrainingDayUpdateComponent>;
    let service: TrainingDayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingDayUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TrainingDayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrainingDayUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainingDayService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TrainingDay(123);
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
        const entity = new TrainingDay();
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
