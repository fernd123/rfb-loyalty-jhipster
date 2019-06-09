import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrainingDay } from 'app/shared/model/training-day.model';
import { TrainingDayService } from './training-day.service';

@Component({
  selector: 'jhi-training-day-delete-dialog',
  templateUrl: './training-day-delete-dialog.component.html'
})
export class TrainingDayDeleteDialogComponent {
  trainingDay: ITrainingDay;

  constructor(
    protected trainingDayService: TrainingDayService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.trainingDayService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'trainingDayListModification',
        content: 'Deleted an trainingDay'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-training-day-delete-popup',
  template: ''
})
export class TrainingDayDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ trainingDay }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TrainingDayDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.trainingDay = trainingDay;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/training-day', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/training-day', { outlets: { popup: null } }]);
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
