import { Moment } from 'moment';
import { ITrainingExercise } from 'app/shared/model/training-exercise.model';

export interface IExercise {
  id?: number;
  creationDate?: Moment;
  name?: string;
  description?: string;
  url?: string;
  exerciseTrainings?: ITrainingExercise[];
}

export class Exercise implements IExercise {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public name?: string,
    public description?: string,
    public url?: string,
    public exerciseTrainings?: ITrainingExercise[]
  ) {}
}
