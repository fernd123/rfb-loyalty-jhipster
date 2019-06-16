import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDiet } from 'app/shared/model/diet.model';

type EntityResponseType = HttpResponse<IDiet>;
type EntityArrayResponseType = HttpResponse<IDiet[]>;

@Injectable({ providedIn: 'root' })
export class DietService {
  public resourceUrl = SERVER_API_URL + 'api/diets';
  public dietSelected: IDiet = null;

  constructor(protected http: HttpClient) {}

  create(diet: IDiet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(diet);
    return this.http
      .post<IDiet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(diet: IDiet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(diet);
    return this.http
      .put<IDiet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDiet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDiet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByCustomerId(id: number, req?: any) {
    const options = createRequestOption(req);

    return this.http
      .get<IDiet[]>(this.resourceUrl + '?customerId.equals=' + id + '&queryParams=customerId&sort=creationDate,desc', {
        params: options,
        observe: 'response'
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(diet: IDiet): IDiet {
    const copy: IDiet = Object.assign({}, diet, {
      creationDate: diet.creationDate != null && diet.creationDate.isValid() ? diet.creationDate.toJSON() : null
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
      res.body.forEach((diet: IDiet) => {
        diet.creationDate = diet.creationDate != null ? moment(diet.creationDate) : null;
      });
    }
    return res;
  }
}
