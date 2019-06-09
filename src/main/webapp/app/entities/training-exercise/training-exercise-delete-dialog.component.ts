import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrainingExercise } from 'app/shared/model/training-exercise.model';
import { TrainingExerciseService } from './training-exercise.service';

@Component({
  selector: 'jhi-training-exercise-delete-dialog',
  templateUrl: './training-exercise-delete-dialog.component.html'
})
export class TrainingExerciseDeleteDialogComponent {
  trainingExercise: ITrainingExercise;

  constructor(
    protected trainingExerciseService: TrainingExerciseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.trainingExerciseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'trainingExerciseListModification',
        content: 'Deleted an trainingExercise'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-training-exercise-delete-popup',
  template: ''
})
export class TrainingExerciseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ trainingExercise }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TrainingExerciseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.trainingExercise = trainingExercise;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/training-exercise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/training-exercise', { outlets: { popup: null } }]);
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
