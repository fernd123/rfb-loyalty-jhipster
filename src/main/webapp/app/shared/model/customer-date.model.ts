import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';

export interface ICustomerDate {
  id?: number;
  creationDate?: Moment;
  observations?: string;
  isActive?: boolean;
  customer?: ICustomer;
}

export class CustomerDate implements ICustomerDate {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public observations?: string,
    public isActive?: boolean,
    public customer?: ICustomer
  ) {
    this.isActive = this.isActive || false;
  }
}
