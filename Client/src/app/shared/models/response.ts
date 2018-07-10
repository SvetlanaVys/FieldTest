import {Field} from './field';

export class Response {
  constructor(
    public id: number,
    public content: string,
    public row: number,
    public field: Field) { }
}
