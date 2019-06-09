import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITrainingExercise } from 'app/shared/model/training-exercise.model';

type EntityResponseType = HttpResponse<ITrainingExercise>;
type EntityArrayResponseType = HttpResponse<ITrainingExercise[]>;

@Injectable({ providedIn: 'root' })
export class TrainingExerciseService {
  public resourceUrl = SERVER_API_URL + 'api/training-exercises';

  constructor(protected http: HttpClient) {}

  create(trainingExercise: ITrainingExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingExercise);
    return this.http
      .post<ITrainingExercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trainingExercise: ITrainingExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingExercise);
    return this.http
      .put<ITrainingExercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrainingExercise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrainingExercise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(trainingExercise: ITrainingExercise): ITrainingExercise {
    const copy: ITrainingExercise = Object.assign({}, trainingExercise, {
      creationDate:
        trainingExercise.creationDate != null && trainingExercise.creationDate.isValid() ? trainingExercise.creationDate.toJSON() : null
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
      res.body.forEach((trainingExercise: ITrainingExercise) => {
        trainingExercise.creationDate = trainingExercise.creationDate != null ? moment(trainingExercise.creationDate) : null;
      });
    }
    return res;
  }
}
