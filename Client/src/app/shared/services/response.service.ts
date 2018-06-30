import {Injectable} from '@angular/core';
import {AppComponent} from '../../app.component';
import {HttpClient} from '@angular/common/http';
import {Response} from '../models/response';

@Injectable()
export class ResponseService {

  constructor(private http: HttpClient) {
  }

  private url = AppComponent.API_URL;

  getResponse() {
    return this.http.get(this.url + '/v1/responses');
  }

  createResponse(response: Response) {
    return this.http.post(this.url + '/v1/responses', response);
  }
}
