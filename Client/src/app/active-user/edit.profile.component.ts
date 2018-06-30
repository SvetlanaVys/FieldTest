import {Component, OnInit} from '@angular/core';
import {User} from '../shared/models/user';
import {Field} from '../shared/models/field';
import {ProfileService} from '../shared/services/profile.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  providers: [ProfileService]
})
export class EditProfileComponent implements OnInit {

  currentUser: User = JSON.parse(localStorage.getItem('currentUser'));

  constructor(private serv: ProfileService) {
  }

  ngOnInit(): void {
  }

  editUser() {
    this.serv.updateUser(this.currentUser.id, this.currentUser).subscribe((data: User) => {
      this.currentUser = data;
      localStorage.setItem('currentUser', JSON.stringify(data));
    });
  }

}
