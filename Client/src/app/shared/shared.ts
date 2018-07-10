import {LoginComponent} from '../login/login.component';
import {RegisterComponent} from '../register/register.component';
import {ActiveUserComponent} from '../active-user/active-user.component';
import {CustomDropdownDirective} from './directives/dropdown-directive';
import {FieldComponent} from '../fields/field.component';
import {EditProfileComponent} from '../active-user/edit.profile.component';
import {FieldService} from './services/field.service';
import {ChangePasswordComponent} from '../active-user/change-password.component';
import {ConfirmEqualValidatorDirective} from './directives/confirm-validator.directive';
import {ResponseService} from './services/response.service';
import {ResponseComponent} from '../response/response.component';
import {CreateResponseComponent} from '../create-response/create-response.component';
import {ProfileService} from './services/profile.service';
import FormattedNullPipe from './pipes/formatted-null.pipe';
import FormattedTimePipe from './pipes/formatted-time.pipe';

const SHARED_COMPONENTS: any[] = [
  LoginComponent,
  RegisterComponent,
  ActiveUserComponent,
  FieldComponent,
  EditProfileComponent,
  ChangePasswordComponent,
  ResponseComponent,
  CreateResponseComponent
];

const SHARED_SERVICES: any[] = [
  FieldService,
  ResponseService,
  ProfileService
];

const SHARED_DIRECTIVES: any[] = [
  CustomDropdownDirective,
  ConfirmEqualValidatorDirective,
];

const SHARED_PIPES: any[] = [
  FormattedNullPipe,
  FormattedTimePipe,
];

export {
  SHARED_COMPONENTS,
  SHARED_SERVICES,
  SHARED_DIRECTIVES,
  SHARED_PIPES,
};
