/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingDayDeleteDialogComponent } from 'app/entities/training-day/training-day-delete-dialog.component';
import { TrainingDayService } from 'app/entities/training-day/training-day.service';

describe('Component Tests', () => {
  describe('TrainingDay Management Delete Component', () => {
    let comp: TrainingDayDeleteDialogComponent;
    let fixture: ComponentFixture<TrainingDayDeleteDialogComponent>;
    let service: TrainingDayService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingDayDeleteDialogComponent]
      })
        .overrideTemplate(TrainingDayDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrainingDayDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainingDayService);
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
