import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDietFood } from 'app/shared/model/diet-food.model';
import { DietFoodService } from './diet-food.service';

@Component({
  selector: 'jhi-diet-food-delete-dialog',
  templateUrl: './diet-food-delete-dialog.component.html'
})
export class DietFoodDeleteDialogComponent {
  dietFood: IDietFood;

  constructor(protected dietFoodService: DietFoodService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.dietFoodService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'dietFoodListModification',
        content: 'Deleted an dietFood'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-diet-food-delete-popup',
  template: ''
})
export class DietFoodDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dietFood }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DietFoodDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.dietFood = dietFood;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/diet-food', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/diet-food', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
