import {Injectable} from '@angular/core';
import {AppComponent} from '../../app.component';
import {HttpClient} from '@angular/common/http';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import {User} from '../models/user';
import {UserLogin} from '../models/userLogin';
import {Option} from '../models/option';

@Injectable()
export class ProfileService {

  constructor(private http: HttpClient) {
  }


  private url = AppComponent.API_URL + '/v1/users';

  public logIn(user: UserLogin) {
    return this.http.post(this.url + '/login', user);
  }

  public logout() {
    localStorage.removeItem('currentUser');
    return true;
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
}
