import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';
import { IDietFood } from 'app/shared/model/diet-food.model';

export interface IDiet {
  id?: number;
  creationDate?: Moment;
  name?: string;
  food1?: string;
  food2?: string;
  food3?: string;
  food4?: string;
  food5?: string;
  food6?: string;
  food7?: string;
  food8?: string;
  food9?: string;
  customer?: ICustomer;
  dietFoods?: IDietFood[];
}

export class Diet implements IDiet {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public name?: string,
    public food1?: string,
    public food2?: string,
    public food3?: string,
    public food4?: string,
    public food5?: string,
    public food6?: string,
    public food7?: string,
    public food8?: string,
    public food9?: string,
    public customer?: ICustomer,
    public dietFoods?: IDietFood[]
  ) {}
}
