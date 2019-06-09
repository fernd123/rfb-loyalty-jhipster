/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingDayComponent } from 'app/entities/training-day/training-day.component';
import { TrainingDayService } from 'app/entities/training-day/training-day.service';
import { TrainingDay } from 'app/shared/model/training-day.model';

describe('Component Tests', () => {
  describe('TrainingDay Management Component', () => {
    let comp: TrainingDayComponent;
    let fixture: ComponentFixture<TrainingDayComponent>;
    let service: TrainingDayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingDayComponent],
        providers: []
      })
        .overrideTemplate(TrainingDayComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrainingDayComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainingDayService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TrainingDay(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.trainingDays[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
