import {Injectable} from '@angular/core';
import {AppComponent} from '../../app.component';
import {HttpClient} from '@angular/common/http';
import {Field} from '../models/field';
import {Option} from '../models/option';

@Injectable()
export class FieldService {

  constructor(private http: HttpClient) {
  }

  private url = AppComponent.API_URL + '/v1/fields';

  getFielts() {
    return this.http.get(this.url);
  }

  getAliveFielts() {
    return this.http.get(this.url + '/alive');
  }

  createField(field: Field) {
    return this.http.post(this.url, field);
  }

  updateField(id: number, field: Field) {
    return this.http.put(this.url + '/' + id, field);
  }

  deleteField(id: number) {
    return this.http.delete(this.url + '/' + id);
  }
/*-------------------OPTIONS----------------*/
  getAllOptions() {
    return this.http.get(this.url + '/options/all');
  }
  getOptionsForField(field: Field) {
    return this.http.post(this.url + '/options', field);
  }

  createOption(option: Option) {
    return this.http.post(this.url + '/options/create', option);
  }

  updateOption(id: number, option: Option) {
    return this.http.put(this.url + '/options/' + id, option);
  }
  deleteOption(id: number) {
    return this.http.delete(this.url + '/options/' + id);
  }
}
