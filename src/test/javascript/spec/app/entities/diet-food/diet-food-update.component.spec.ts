/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { DietFoodUpdateComponent } from 'app/entities/diet-food/diet-food-update.component';
import { DietFoodService } from 'app/entities/diet-food/diet-food.service';
import { DietFood } from 'app/shared/model/diet-food.model';

describe('Component Tests', () => {
  describe('DietFood Management Update Component', () => {
    let comp: DietFoodUpdateComponent;
    let fixture: ComponentFixture<DietFoodUpdateComponent>;
    let service: DietFoodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [DietFoodUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DietFoodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DietFoodUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DietFoodService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DietFood(123);
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
        const entity = new DietFood();
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
