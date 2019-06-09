import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';
import { ITrainingDay } from 'app/shared/model/training-day.model';

export interface ITraining {
  id?: number;
  creationDate?: Moment;
  name?: string;
  customer?: ICustomer;
  trainingDays?: ITrainingDay[];
}

export class Training implements ITraining {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public name?: string,
    public customer?: ICustomer,
    public trainingDays?: ITrainingDay[]
  ) {}
}
