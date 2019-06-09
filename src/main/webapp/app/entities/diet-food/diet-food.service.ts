import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDietFood } from 'app/shared/model/diet-food.model';

type EntityResponseType = HttpResponse<IDietFood>;
type EntityArrayResponseType = HttpResponse<IDietFood[]>;

@Injectable({ providedIn: 'root' })
export class DietFoodService {
  public resourceUrl = SERVER_API_URL + 'api/diet-foods';

  constructor(protected http: HttpClient) {}

  create(dietFood: IDietFood): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dietFood);
    return this.http
      .post<IDietFood>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dietFood: IDietFood): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dietFood);
    return this.http
      .put<IDietFood>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDietFood>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDietFood[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(dietFood: IDietFood): IDietFood {
    const copy: IDietFood = Object.assign({}, dietFood, {
      creationDate: dietFood.creationDate != null && dietFood.creationDate.isValid() ? dietFood.creationDate.toJSON() : null
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
      res.body.forEach((dietFood: IDietFood) => {
        dietFood.creationDate = dietFood.creationDate != null ? moment(dietFood.creationDate) : null;
      });
    }
    return res;
  }
}
