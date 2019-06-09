import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerDate } from 'app/shared/model/customer-date.model';
import { CustomerDateService } from './customer-date.service';

@Component({
  selector: 'jhi-customer-date-delete-dialog',
  templateUrl: './customer-date-delete-dialog.component.html'
})
export class CustomerDateDeleteDialogComponent {
  customerDate: ICustomerDate;

  constructor(
    protected customerDateService: CustomerDateService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.customerDateService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'customerDateListModification',
        content: 'Deleted an customerDate'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-customer-date-delete-popup',
  template: ''
})
export class CustomerDateDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customerDate }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerDateDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.customerDate = customerDate;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/customer-date', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/customer-date', { outlets: { popup: null } }]);
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
