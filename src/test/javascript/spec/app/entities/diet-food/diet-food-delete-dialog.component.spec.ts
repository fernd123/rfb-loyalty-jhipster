/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RfbloyaltyTestModule } from '../../../test.module';
import { DietFoodDeleteDialogComponent } from 'app/entities/diet-food/diet-food-delete-dialog.component';
import { DietFoodService } from 'app/entities/diet-food/diet-food.service';

describe('Component Tests', () => {
  describe('DietFood Management Delete Component', () => {
    let comp: DietFoodDeleteDialogComponent;
    let fixture: ComponentFixture<DietFoodDeleteDialogComponent>;
    let service: DietFoodService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [DietFoodDeleteDialogComponent]
      })
        .overrideTemplate(DietFoodDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DietFoodDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DietFoodService);
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
