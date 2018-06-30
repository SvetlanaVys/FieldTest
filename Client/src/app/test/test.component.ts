import { Component } from '@angular/core';
import { WebSocketSubject } from 'rxjs/observable/dom/WebSocketSubject';
import * as url from 'url';
import {TestService} from '../shared/services/test.service';
import {Field} from '../shared/models/field';

export class Message {
  constructor(
    public sender: string,
    public content: string,
    public isBroadcast = false,
  ) { }
}



@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css'],
  providers: [ TestService ]
})
export class TestComponent {

  message: string;

  field: Field;

  constructor(private serv: TestService) {
    this.message = '';
    serv.start('ws://localhost:9000');
  }

  send() {
    // alert(this.message);
    this.field = new Field(0, 'title', 'button', true, true, false);
    this.serv.sendMessage(this.field);
  }

  // private socket$: WebSocketSubject<String>;
  // serverMessages: String[];
  //
  // constructor() {
  //   this.socket$ = WebSocketSubject.create('ws://localhost:9000');
  //
  //   this.socket$
  //     .subscribe(
  //       (message) => this.serverMessages.push(message),
  //       (err) => console.error(err),
  //       () => console.warn('Completed!')
  //     );
  // }
}
