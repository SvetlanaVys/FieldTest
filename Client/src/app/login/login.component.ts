import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {User} from '../shared/models/user';
import {ProfileService} from '../shared/services/profile.service';
import {UserLogin} from '../shared/models/userLogin';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  providers: [ProfileService]
})
export class LoginComponent implements OnInit {

  // user: User;
  userLogin: UserLogin;
  errorMessage: string;
  some: any;
  constructor(private serv: ProfileService, public router: Router) {
    this.userLogin = new UserLogin('', '');
  }

  ngOnInit() {
  }

  login() {
    this.serv.logIn(this.userLogin).subscribe((data: User) => {
        localStorage.setItem('currentUser', JSON.stringify(data));
      this.router.navigate(['/profile']);
    }, err => {
      this.errorMessage = 'Wrong e-mail or password';
    });
  }

}
