import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMeasure } from 'app/shared/model/measure.model';

type EntityResponseType = HttpResponse<IMeasure>;
type EntityArrayResponseType = HttpResponse<IMeasure[]>;

@Injectable({ providedIn: 'root' })
export class MeasureService {
  public resourceUrl = SERVER_API_URL + 'api/measures';

  constructor(protected http: HttpClient) {}

  create(measure: IMeasure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measure);
    return this.http
      .post<IMeasure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(measure: IMeasure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measure);
    return this.http
      .put<IMeasure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMeasure>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeasure[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(measure: IMeasure): IMeasure {
    const copy: IMeasure = Object.assign({}, measure, {
      creationDate: measure.creationDate != null && measure.creationDate.isValid() ? measure.creationDate.toJSON() : null
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
      res.body.forEach((measure: IMeasure) => {
        measure.creationDate = measure.creationDate != null ? moment(measure.creationDate) : null;
      });
    }
    return res;
  }
}
