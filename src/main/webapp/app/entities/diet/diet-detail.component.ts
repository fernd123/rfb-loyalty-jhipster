import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDiet } from 'app/shared/model/diet.model';

@Component({
  selector: 'jhi-diet-detail',
  templateUrl: './diet-detail.component.html'
})
export class DietDetailComponent implements OnInit {
  diet: IDiet;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ diet }) => {
      this.diet = diet;
    });
  }

  previousState() {
    window.history.back();
  }
}
