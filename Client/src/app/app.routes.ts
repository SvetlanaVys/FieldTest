import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {ActiveUserComponent} from './active-user/active-user.component';
import {CreateResponseComponent} from './create-response/create-response.component';
import {UrlPermission} from './shared/guard/url.permission';

const appRoutes: Routes = [
  { path: '', component: CreateResponseComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ActiveUserComponent, canActivate: [UrlPermission] },

  { path: '**', redirectTo: '/' }
];

export const routing = RouterModule.forRoot(appRoutes);
