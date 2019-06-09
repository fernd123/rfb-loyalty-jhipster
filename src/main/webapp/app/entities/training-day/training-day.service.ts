import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITrainingDay } from 'app/shared/model/training-day.model';

type EntityResponseType = HttpResponse<ITrainingDay>;
type EntityArrayResponseType = HttpResponse<ITrainingDay[]>;

@Injectable({ providedIn: 'root' })
export class TrainingDayService {
  public resourceUrl = SERVER_API_URL + 'api/training-days';

  constructor(protected http: HttpClient) {}

  create(trainingDay: ITrainingDay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingDay);
    return this.http
      .post<ITrainingDay>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trainingDay: ITrainingDay): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingDay);
    return this.http
      .put<ITrainingDay>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrainingDay>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrainingDay[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(trainingDay: ITrainingDay): ITrainingDay {
    const copy: ITrainingDay = Object.assign({}, trainingDay, {
      creationDate: trainingDay.creationDate != null && trainingDay.creationDate.isValid() ? trainingDay.creationDate.toJSON() : null
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
      res.body.forEach((trainingDay: ITrainingDay) => {
        trainingDay.creationDate = trainingDay.creationDate != null ? moment(trainingDay.creationDate) : null;
      });
    }
    return res;
  }
}
