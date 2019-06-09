import { Moment } from 'moment';
import { IMeasure } from 'app/shared/model/measure.model';
import { ITraining } from 'app/shared/model/training.model';
import { IDiet } from 'app/shared/model/diet.model';
import { ICustomerDate } from 'app/shared/model/customer-date.model';

export const enum Sex {
  MASCULINO = 'MASCULINO',
  FEMENINO = 'FEMENINO'
}

export const enum Goal {
  MUSCULACION = 'MUSCULACION',
  FITNESS = 'FITNESS',
  CULTURISMO = 'CULTURISMO',
  DEPORTE_CONTACTO = 'DEPORTE_CONTACTO'
}

export interface ICustomer {
  id?: number;
  name?: string;
  firstName?: string;
  birthDate?: Moment;
  sex?: Sex;
  phone?: number;
  email?: string;
  objective?: Goal;
  observations?: string;
  creationDate?: Moment;
  isActive?: boolean;
  customerMeasures?: IMeasure[];
  customerTrainings?: ITraining[];
  customerDiets?: IDiet[];
  customerDates?: ICustomerDate[];
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public name?: string,
    public firstName?: string,
    public birthDate?: Moment,
    public sex?: Sex,
    public phone?: number,
    public email?: string,
    public objective?: Goal,
    public observations?: string,
    public creationDate?: Moment,
    public isActive?: boolean,
    public customerMeasures?: IMeasure[],
    public customerTrainings?: ITraining[],
    public customerDiets?: IDiet[],
    public customerDates?: ICustomerDate[]
  ) {
    this.isActive = this.isActive || false;
  }
}
