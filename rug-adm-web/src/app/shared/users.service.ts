import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { User } from "./user.model";
import { environment } from "../../environments/environment";
import { Subject } from "rxjs/Subject";
import { tokenNotExpired } from 'angular2-jwt';

@Injectable()
export class UsersService {
  users: User[] = [];
  usersChanged = new Subject<User[]>();
  authenticatedUser: User;

  constructor(private http: HttpClient) {
    this.authenticatedUser = JSON.parse(localStorage.getItem('currentUser'));
    if (this.authenticatedUser != null && !tokenNotExpired(null, this.authenticatedUser.token)) {
      this.authenticatedUser = null;
    }
  }

  getUsers() {
    return this.users.slice();
  }

  addUser(user: User) {
    this.saveData(user).subscribe(
      (response) => {
        let savedUser: User = response;
        this.users.push(savedUser);
        this.usersChanged.next(this.getUsers());
      },
      err => {
        console.log(err);
      }
    );
  }

  addUsers(users: User[]) {
    this.users.push(...users);
    this.usersChanged.next(this.getUsers());
  }

  updateUser(index: number, newUser: User) {
    this.updateData(newUser.usuarioId, newUser).subscribe(
      (response) => {
        let updatedUser: User = response;
        if (newUser.estado == 'I') {
          this.users.splice(index, 1);
        } else {
          this.users[index] = updatedUser;
        }
        this.usersChanged.next(this.getUsers());
      }
    );
  }

  login(email: string, password: string) {
    console.log('Login: ' , environment.api_url + '/usuarios/login' )
    return this.http.post<User>(environment.api_url + '/usuarios/login', {
      email: email,
      password
    });
  }

  logout() {
    return this.http.post(environment.api_url + '/usuarios/logout', {
      token: this.authenticatedUser.token
    });
  }

  fetchData() {
    return this.http.get<User[]>(environment.api_url + '/usuarios');
  }

  saveData(user: User) {
    return this.http.post<User>(environment.api_url + '/usuarios', user);
  }

  updateData(id: number, user: User) {
    return this.http.put<User>(environment.api_url + '/usuarios/' + id, user);
  }
}
