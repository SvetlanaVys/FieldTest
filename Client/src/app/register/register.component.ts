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
  message: string;
  errorMessage: string;
  constructor(private serv: ProfileService, public router: Router) {
    this.user = new User(0, '', '', '', '', '');
    this.currentUser = new CurrentUser(0, '', '', '', '', '', '');

  }

  ngOnInit() {
  }

  createAccount() {
    this.makeUser(this.currentUser);
    this.serv.createUser(this.user).subscribe(data => {
      // localStorage.setItem('currentUser', JSON.stringify(data));
      alert('Account was created' + data);
    }, err => {
      this.errorMessage = 'Email already exist';
    });

  }

  makeUser(currentUser: CurrentUser) {

    this.user.id = currentUser.id;
    this.user.email = currentUser.email;
    this.user.password = currentUser.password;
    this.user.firstName = currentUser.firstName;
    this.user.lastName = currentUser.lastName;
    this.user.phone = currentUser.phone;
  }
}
