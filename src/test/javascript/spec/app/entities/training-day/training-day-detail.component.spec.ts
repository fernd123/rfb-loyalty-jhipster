/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { TrainingDayDetailComponent } from 'app/entities/training-day/training-day-detail.component';
import { TrainingDay } from 'app/shared/model/training-day.model';

describe('Component Tests', () => {
  describe('TrainingDay Management Detail Component', () => {
    let comp: TrainingDayDetailComponent;
    let fixture: ComponentFixture<TrainingDayDetailComponent>;
    const route = ({ data: of({ trainingDay: new TrainingDay(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [TrainingDayDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TrainingDayDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrainingDayDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.trainingDay).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
