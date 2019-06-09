/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RfbloyaltyTestModule } from '../../../test.module';
import { CustomerDateComponent } from 'app/entities/customer-date/customer-date.component';
import { CustomerDateService } from 'app/entities/customer-date/customer-date.service';
import { CustomerDate } from 'app/shared/model/customer-date.model';

describe('Component Tests', () => {
  describe('CustomerDate Management Component', () => {
    let comp: CustomerDateComponent;
    let fixture: ComponentFixture<CustomerDateComponent>;
    let service: CustomerDateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [CustomerDateComponent],
        providers: []
      })
        .overrideTemplate(CustomerDateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomerDateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CustomerDateService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CustomerDate(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.customerDates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
