import {Field} from './field';

export class Option {
  constructor(
    public id: number,
    public name: string,
    public field: Field) { }
}
