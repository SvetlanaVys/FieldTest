import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {TestComponent} from './test/test.component';
import {ActiveUserComponent} from './active-user/active-user.component';
import {FieldComponent} from './fields/field.component';
import {CreateResponseComponent} from './create-response/create-response.component';
import {UrlPermission} from './shared/guard/url.permission';
import {ChangePasswordComponent} from './active-user/change-password.component';

const appRoutes: Routes = [
  { path: '', component: CreateResponseComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'test', component: TestComponent},
  { path: 'profile', component: ActiveUserComponent, canActivate: [UrlPermission] },
  // { path: 'change', component: ChangePasswordComponent, canActivate: [UrlPermission] },
  // { path: 'profile', component: ProfileComponent, canActivate: [UrlPermission] },

  { path: '**', redirectTo: '/' }
];

export const routing = RouterModule.forRoot(appRoutes);
