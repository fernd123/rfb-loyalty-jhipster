import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerDate } from 'app/shared/model/customer-date.model';

type EntityResponseType = HttpResponse<ICustomerDate>;
type EntityArrayResponseType = HttpResponse<ICustomerDate[]>;

@Injectable({ providedIn: 'root' })
export class CustomerDateService {
  public resourceUrl = SERVER_API_URL + 'api/customer-dates';

  constructor(protected http: HttpClient) {}

  create(customerDate: ICustomerDate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerDate);
    return this.http
      .post<ICustomerDate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(customerDate: ICustomerDate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerDate);
    return this.http
      .put<ICustomerDate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICustomerDate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICustomerDate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(customerDate: ICustomerDate): ICustomerDate {
    const copy: ICustomerDate = Object.assign({}, customerDate, {
      creationDate: customerDate.creationDate != null && customerDate.creationDate.isValid() ? customerDate.creationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((customerDate: ICustomerDate) => {
        customerDate.creationDate = customerDate.creationDate != null ? moment(customerDate.creationDate) : null;
      });
    }
    return res;
  }
}
