import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDiet } from 'app/shared/model/diet.model';
import { DietService } from './diet.service';

@Component({
  selector: 'jhi-diet-delete-dialog',
  templateUrl: './diet-delete-dialog.component.html'
})
export class DietDeleteDialogComponent {
  diet: IDiet;

  constructor(protected dietService: DietService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.dietService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'dietListModification',
        content: 'Deleted an diet'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-diet-delete-popup',
  template: ''
})
export class DietDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ diet }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DietDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.diet = diet;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/diet', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/diet', { outlets: { popup: null } }]);
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
