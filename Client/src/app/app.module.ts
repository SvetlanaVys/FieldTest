import { BrowserModule } from '@angular/platform-browser';
import { NgModule} from '@angular/core';

import { FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';

import {SHARED_COMPONENTS, SHARED_SERVICES, SHARED_DIRECTIVES, SHARED_PIPES} from './shared/shared';

import {routing} from './app.routes';
import { AppComponent } from './app.component';
import {HttpClientModule} from '@angular/common/http';
import { NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ModalModule } from 'ngx-bootstrap';
import {UrlPermission} from './shared/guard/url.permission';


@NgModule({
  declarations: [
    AppComponent,
    SHARED_COMPONENTS,
    SHARED_DIRECTIVES,
    SHARED_PIPES,
  ],
  imports: [
    BrowserModule,
    routing,
    FormsModule,
    HttpClientModule,
    NgbModule.forRoot(),
    ModalModule.forRoot(),
  ],
  providers: [SHARED_SERVICES, UrlPermission],
  bootstrap: [AppComponent]
})
export class AppModule { }
