import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ProfileService} from '../shared/services/profile.service';
import {User} from '../shared/models/user';

@Component({
  selector: 'app-active-user',
  templateUrl: './active-user.component.html',
  styleUrls: ['./active-user.component.css'],
  providers: [ProfileService]
})
export class ActiveUserComponent {

  component: any;
  isShow: boolean;
  currentUser: User = JSON.parse(localStorage.getItem('currentUser'));

  constructor(private serv: ProfileService, public router: Router) {
    this.isShow = false;
    this.component = 'field';
  }

  show() {
    this.isShow = !this.isShow;
  }

  selectComponent(component: String) {
    this.component = component;
  }

  logout() {
    this.serv.logout().subscribe(data => {
      localStorage.removeItem('currentUser');
      this.router.navigate(['/login']);
    });
  }
}
