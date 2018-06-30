export class CurrentUser {
  constructor(
    public id: number,
    public email: string,
    public password: string,
    public confirmPassword: string,
    public firstName: string,
    public lastName: string,
    public phone: string) { }
}
