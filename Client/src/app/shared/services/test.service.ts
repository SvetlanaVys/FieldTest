import { Injectable } from '@angular/core';
import { Subject, Observable, Subscription } from 'rxjs/Rx';
import { WebSocketSubject } from 'rxjs/observable/dom/WebSocketSubject';
import {Field} from '../models/field';

@Injectable()
export class TestService {

  private ws: WebSocketSubject<Object>;
  private socket: Subscription;
  private url: string;

  public message: Subject<Object> = new Subject();
  public opened: Subject<boolean> = new Subject();

  public close(): void {
    this.socket.unsubscribe();
    this.ws.complete();
  }

  public sendMessage( message: Field): void {
    this.ws.next( message );
  }

  public start( url: string ): void {
    const self = this;

    this.url = url;

    this.ws = Observable.webSocket( this.url );
    console.log(this.message);
    this.socket = this.ws.subscribe( {
      next: ( data: MessageEvent ) => {
        console.log(data);
        if ( data[ 'type' ] === 'welcome' ) {
          self.opened.next( true );
        }
        this.message.next( data );
      },
      error: () => {

        self.opened.next( false );
        this.message.next( { type: 'closed' } );

        self.socket.unsubscribe();

        setTimeout( () => {
          self.start( self.url );
        }, 1000 );

      },
      complete: () => {
        this.message.next( { type: 'closed' } );
      }

    } );

  }

}

// import {Injectable} from '@angular/core';
// import * as socketIo from 'socket.io-client';
// import {AppComponent} from '../../app.component';
// import {HttpClient} from '@angular/common/http';
//
//
// @Injectable()
// export class TestService {
//
//   private url = AppComponent.API_URL;
//   private socket;
//
//   constructor(private http: HttpClient) {
//   }
//
//   public initSocket(): void {
//     alert(this.url);
//     this.socket = socketIo(this.url);
//   }
// }
//
