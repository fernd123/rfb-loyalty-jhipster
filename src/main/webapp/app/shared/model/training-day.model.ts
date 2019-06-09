import { Moment } from 'moment';
import { ITraining } from 'app/shared/model/training.model';
import { ITrainingExercise } from 'app/shared/model/training-exercise.model';

export interface ITrainingDay {
  id?: number;
  creationDate?: Moment;
  name?: string;
  training?: ITraining;
  trainingExercises?: ITrainingExercise[];
}

export class TrainingDay implements ITrainingDay {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public name?: string,
    public training?: ITraining,
    public trainingExercises?: ITrainingExercise[]
  ) {}
}
