export class Field {
  constructor(
    public id: number,
    public label: string,
    public type: string,
    public required: boolean,
    public isActive: boolean,
    public isDeleted: boolean) { }
}
