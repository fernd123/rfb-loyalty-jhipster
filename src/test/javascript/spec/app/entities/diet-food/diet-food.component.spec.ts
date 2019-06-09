/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RfbloyaltyTestModule } from '../../../test.module';
import { DietFoodComponent } from 'app/entities/diet-food/diet-food.component';
import { DietFoodService } from 'app/entities/diet-food/diet-food.service';
import { DietFood } from 'app/shared/model/diet-food.model';

describe('Component Tests', () => {
  describe('DietFood Management Component', () => {
    let comp: DietFoodComponent;
    let fixture: ComponentFixture<DietFoodComponent>;
    let service: DietFoodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [DietFoodComponent],
        providers: []
      })
        .overrideTemplate(DietFoodComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DietFoodComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DietFoodService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DietFood(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dietFoods[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
