/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { DietFoodDetailComponent } from 'app/entities/diet-food/diet-food-detail.component';
import { DietFood } from 'app/shared/model/diet-food.model';

describe('Component Tests', () => {
  describe('DietFood Management Detail Component', () => {
    let comp: DietFoodDetailComponent;
    let fixture: ComponentFixture<DietFoodDetailComponent>;
    const route = ({ data: of({ dietFood: new DietFood(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [DietFoodDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DietFoodDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DietFoodDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dietFood).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
