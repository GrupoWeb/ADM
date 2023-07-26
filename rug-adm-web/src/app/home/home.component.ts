import { Component, OnInit } from '@angular/core';
import { User } from '../shared/user.model';
import { UsersService } from '../shared/users.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user: User;

  constructor(private usersService: UsersService) { }

  ngOnInit() {
    this.user = this.usersService.authenticatedUser;
  }

}
