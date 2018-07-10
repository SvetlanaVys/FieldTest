import {Injectable} from '@angular/core';
import {AppComponent} from '../../app.component';
import {HttpClient} from '@angular/common/http';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import {User} from '../models/user';
import {UserLogin} from '../models/userLogin';
import {Password} from '../models/password';

@Injectable()
export class ProfileService {

  constructor(private http: HttpClient) {
  }


  private url = AppComponent.API_URL + '/v1/users';

  public logIn(user: UserLogin) {
    return this.http.post(this.url + '/login', user);
  }

  public logout() {
    return this.http.get(this.url + '/logout');
  }

  getFielts() {
    return this.http.get(this.url);
  }

  createUser(user: User) {
    return this.http.post(this.url, user);
  }

  updateUser(id: number, user: User) {
    return this.http.put(this.url + '/' + id, user);
  }

  updateUserPassword(id: number, password: Password) {
    return this.http.put(this.url + '/password/' + id, password);
  }
}
