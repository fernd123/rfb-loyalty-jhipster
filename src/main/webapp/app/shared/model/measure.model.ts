import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';

export interface IMeasure {
  id?: number;
  creationDate?: Moment;
  arm?: number;
  ribCage?: number;
  leg?: number;
  customer?: ICustomer;
}

export class Measure implements IMeasure {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public arm?: number,
    public ribCage?: number,
    public leg?: number,
    public customer?: ICustomer
  ) {}
}
