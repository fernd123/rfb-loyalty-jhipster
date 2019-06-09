/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { CustomerDateUpdateComponent } from 'app/entities/customer-date/customer-date-update.component';
import { CustomerDateService } from 'app/entities/customer-date/customer-date.service';
import { CustomerDate } from 'app/shared/model/customer-date.model';

describe('Component Tests', () => {
  describe('CustomerDate Management Update Component', () => {
    let comp: CustomerDateUpdateComponent;
    let fixture: ComponentFixture<CustomerDateUpdateComponent>;
    let service: CustomerDateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [CustomerDateUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CustomerDateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerDateUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerDateService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CustomerDate(123);
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
        const entity = new CustomerDate();
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
