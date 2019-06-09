import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITraining } from 'app/shared/model/training.model';
import { TrainingService } from './training.service';

@Component({
  selector: 'jhi-training-delete-dialog',
  templateUrl: './training-delete-dialog.component.html'
})
export class TrainingDeleteDialogComponent {
  training: ITraining;

  constructor(protected trainingService: TrainingService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.trainingService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'trainingListModification',
        content: 'Deleted an training'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-training-delete-popup',
  template: ''
})
export class TrainingDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ training }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TrainingDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.training = training;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/training', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/training', { outlets: { popup: null } }]);
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
