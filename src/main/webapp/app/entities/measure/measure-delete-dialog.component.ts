import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMeasure } from 'app/shared/model/measure.model';
import { MeasureService } from './measure.service';

@Component({
  selector: 'jhi-measure-delete-dialog',
  templateUrl: './measure-delete-dialog.component.html'
})
export class MeasureDeleteDialogComponent {
  measure: IMeasure;

  constructor(protected measureService: MeasureService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.measureService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'measureListModification',
        content: 'Deleted an measure'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-measure-delete-popup',
  template: ''
})
export class MeasureDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ measure }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MeasureDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.measure = measure;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/measure', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/measure', { outlets: { popup: null } }]);
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
