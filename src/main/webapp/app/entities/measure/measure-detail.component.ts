import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMeasure } from 'app/shared/model/measure.model';

@Component({
  selector: 'jhi-measure-detail',
  templateUrl: './measure-detail.component.html'
})
export class MeasureDetailComponent implements OnInit {
  measure: IMeasure;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ measure }) => {
      this.measure = measure;
    });
  }

  previousState() {
    window.history.back();
  }
}
