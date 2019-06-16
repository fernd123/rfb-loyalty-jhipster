import { TrainingService } from 'app/entities/training/training.service';
import { MeasureService } from 'app/entities/measure/measure.service';
import { Customer } from './../../shared/model/customer.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, zip, forkJoin } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomer } from 'app/shared/model/customer.model';
import { DietService } from '../diet';

type EntityResponseType = HttpResponse<ICustomer>;
type EntityArrayResponseType = HttpResponse<ICustomer[]>;

@Injectable({ providedIn: 'root' })
export class CustomerService {
  public customer: ICustomer = new Customer();
  public resourceUrl = SERVER_API_URL + 'api/customers';

  constructor(
    protected http: HttpClient,
    private measureService: MeasureService,
    private trainingService: TrainingService,
    private dietService: DietService
  ) {}

  create(customer: ICustomer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customer);
    return this.http
      .post<ICustomer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(customer: ICustomer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customer);
    return this.http
      .put<ICustomer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICustomer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findByKeyword(keyword: string, req?: any) {
    const options = createRequestOption(req);

    return this.http
      .get<ICustomer[]>(this.resourceUrl + '?name.contains=' + keyword + '&queryParams=name', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  refreshAll() {
    forkJoin([
      this.measureService.findByCustomerId(this.customer.id, {}),
      this.trainingService.findByCustomerId(this.customer.id, {}),
      this.dietService.findByCustomerId(this.customer.id, {})
    ]).subscribe(results => {
      this.customer.customerMeasures = results[0].body;
      this.customer.customerTrainings = results[1].body;
      this.customer.customerDiets = results[2].body;
    });
  }

  protected convertDateFromClient(customer: ICustomer): ICustomer {
    const copy: ICustomer = Object.assign({}, customer, {
      birthDate: customer.birthDate != null && customer.birthDate.isValid() ? customer.birthDate.toJSON() : null,
      creationDate: customer.creationDate != null && customer.creationDate.isValid() ? customer.creationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate != null ? moment(res.body.birthDate) : null;
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((customer: ICustomer) => {
        customer.birthDate = customer.birthDate != null ? moment(customer.birthDate) : null;
        customer.creationDate = customer.creationDate != null ? moment(customer.creationDate) : null;
      });
    }
    return res;
  }
}
