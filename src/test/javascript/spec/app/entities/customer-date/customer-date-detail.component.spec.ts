/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RfbloyaltyTestModule } from '../../../test.module';
import { CustomerDateDetailComponent } from 'app/entities/customer-date/customer-date-detail.component';
import { CustomerDate } from 'app/shared/model/customer-date.model';

describe('Component Tests', () => {
  describe('CustomerDate Management Detail Component', () => {
    let comp: CustomerDateDetailComponent;
    let fixture: ComponentFixture<CustomerDateDetailComponent>;
    const route = ({ data: of({ customerDate: new CustomerDate(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RfbloyaltyTestModule],
        declarations: [CustomerDateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CustomerDateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomerDateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.customerDate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
