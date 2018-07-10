import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {User} from '../shared/models/user';
import {CurrentUser} from '../shared/models/current-user';
import {ProfileService} from '../shared/services/profile.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  providers: [ProfileService]
})
export class RegisterComponent implements OnInit {

  user: User;
  currentUser: CurrentUser;
  confPassword: string;
  message: string;
  success: boolean;

  constructor(private serv: ProfileService, public router: Router) {
    this.user = new User(0, '', '', '', '', '');
    this.confPassword = '';
    this.success = false;
  }

  ngOnInit() {
  }

  createAccount() {
    this.user.id = null;
    this.serv.createUser(this.user).subscribe(data => {
      this.message = 'Account was created success';
      this.success = true;
    }, err => {
      this.message = 'Email already exist';
      this.success = false;
    });

  }

}
