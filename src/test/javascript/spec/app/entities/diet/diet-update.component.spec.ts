/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { DietUpdateComponent } from 'app/entities/diet/diet-update.component';
import { DietService } from 'app/entities/diet/diet.service';
import { Diet } from 'app/shared/model/diet.model';

describe('Component Tests', () => {
  describe('Diet Management Update Component', () => {
    let comp: DietUpdateComponent;
    let fixture: ComponentFixture<DietUpdateComponent>;
    let service: DietService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [DietUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DietUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DietUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DietService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Diet(123);
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
        const entity = new Diet();
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
