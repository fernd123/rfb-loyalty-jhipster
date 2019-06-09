import { Moment } from 'moment';
import { ITrainingDay } from 'app/shared/model/training-day.model';
import { IExercise } from 'app/shared/model/exercise.model';

export interface ITrainingExercise {
  id?: number;
  creationDate?: Moment;
  trainingNumber?: number;
  description?: string;
  trainingDay?: ITrainingDay;
  exercise?: IExercise;
}

export class TrainingExercise implements ITrainingExercise {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public trainingNumber?: number,
    public description?: string,
    public trainingDay?: ITrainingDay,
    public exercise?: IExercise
  ) {}
}
