import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITraining } from 'app/shared/model/training.model';

type EntityResponseType = HttpResponse<ITraining>;
type EntityArrayResponseType = HttpResponse<ITraining[]>;

@Injectable({ providedIn: 'root' })
export class TrainingService {
  public resourceUrl = SERVER_API_URL + 'api/trainings';

  constructor(protected http: HttpClient) {}

  create(training: ITraining): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(training);
    return this.http
      .post<ITraining>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(training: ITraining): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(training);
    return this.http
      .put<ITraining>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITraining>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITraining[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByCustomerId(id: number, req?: any) {
    const options = createRequestOption(req);

    return this.http
      .get<ITraining[]>(this.resourceUrl + '?customerId.equals=' + id + '&queryParams=customerId&sort=creationDate,desc', {
        params: options,
        observe: 'response'
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(training: ITraining): ITraining {
    const copy: ITraining = Object.assign({}, training, {
      creationDate: training.creationDate != null && training.creationDate.isValid() ? training.creationDate.toJSON() : null
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
      res.body.forEach((training: ITraining) => {
        training.creationDate = training.creationDate != null ? moment(training.creationDate) : null;
      });
    }
    return res;
  }
}
