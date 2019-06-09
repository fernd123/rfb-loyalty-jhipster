import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDietFood } from 'app/shared/model/diet-food.model';

@Component({
  selector: 'jhi-diet-food-detail',
  templateUrl: './diet-food-detail.component.html'
})
export class DietFoodDetailComponent implements OnInit {
  dietFood: IDietFood;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dietFood }) => {
      this.dietFood = dietFood;
    });
  }

  previousState() {
    window.history.back();
  }
}
