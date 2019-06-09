import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';
import { IDietFood } from 'app/shared/model/diet-food.model';

export interface IDiet {
  id?: number;
  creationDate?: Moment;
  name?: string;
  customer?: ICustomer;
  dietFoods?: IDietFood[];
}

export class Diet implements IDiet {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public name?: string,
    public customer?: ICustomer,
    public dietFoods?: IDietFood[]
  ) {}
}
