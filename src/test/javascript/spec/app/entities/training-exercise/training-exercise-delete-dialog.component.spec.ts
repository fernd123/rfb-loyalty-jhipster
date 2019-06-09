/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingExerciseDeleteDialogComponent } from 'app/entities/training-exercise/training-exercise-delete-dialog.component';
import { TrainingExerciseService } from 'app/entities/training-exercise/training-exercise.service';

describe('Component Tests', () => {
  describe('TrainingExercise Management Delete Component', () => {
    let comp: TrainingExerciseDeleteDialogComponent;
    let fixture: ComponentFixture<TrainingExerciseDeleteDialogComponent>;
    let service: TrainingExerciseService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingExerciseDeleteDialogComponent]
      })
        .overrideTemplate(TrainingExerciseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrainingExerciseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainingExerciseService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
