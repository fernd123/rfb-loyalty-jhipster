import { Moment } from 'moment';
import { IDiet } from 'app/shared/model/diet.model';

export interface IDietFood {
  id?: number;
  creationDate?: Moment;
  description?: string;
  diet?: IDiet;
}

export class DietFood implements IDietFood {
  constructor(public id?: number, public creationDate?: Moment, public description?: string, public diet?: IDiet) {}
}
