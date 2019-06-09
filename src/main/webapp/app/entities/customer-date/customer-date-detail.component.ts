import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerDate } from 'app/shared/model/customer-date.model';

@Component({
  selector: 'jhi-customer-date-detail',
  templateUrl: './customer-date-detail.component.html'
})
export class CustomerDateDetailComponent implements OnInit {
  customerDate: ICustomerDate;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customerDate }) => {
      this.customerDate = customerDate;
    });
  }

  previousState() {
    window.history.back();
  }
}
