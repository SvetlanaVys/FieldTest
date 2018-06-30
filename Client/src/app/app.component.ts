import { Component } from '@angular/core';

@Component({
  selector: 'app-start',
  templateUrl: 'app.component.html'
})
export class AppComponent {
  static WS_URL = 'ws://localhost:9000';
  static API_URL = 'http://localhost:9000';
}
