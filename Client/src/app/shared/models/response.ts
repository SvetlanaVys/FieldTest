import {Field} from './field';

export class Response {
  constructor(
    public id: number,
    public content: string,
    public field: Field,
    public row: number) { }
}
