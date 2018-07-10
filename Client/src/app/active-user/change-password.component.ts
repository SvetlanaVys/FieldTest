import {Component, OnInit} from '@angular/core';
import {Password} from '../shared/models/password';
import {User} from '../shared/models/user';
import {ProfileService} from '../shared/services/profile.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
})
export class ChangePasswordComponent implements OnInit {

  passwords: Password;
  currentUser: User = JSON.parse(localStorage.getItem('currentUser'));
  confPassword: string;
  message: string;

  constructor(private serv: ProfileService,  public router: Router) {
    this.passwords = new Password('', '');
    this.confPassword = '';
  }

  ngOnInit(): void {
  }

  change() {
    this.serv.updateUserPassword(this.currentUser.id, this.passwords).subscribe(data => {
      this.serv.logout().subscribe(datalog => {
        localStorage.removeItem('currentUser');
        this.router.navigate(['/login']);
      });
    }, err => {
      this.message = 'Incorrect password';
    });
  }
}
