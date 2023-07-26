import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UsersService } from '../shared/users.service';
import { User } from '../shared/user.model';
import { Router } from '@angular/router';
import { LoadingService } from '../shared/loading.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public loading = false;
  @ViewChild('f') loginForm: NgForm;
  error: string;

  constructor(private usersService: UsersService,
    private loadingService: LoadingService,
    private router: Router) { }

  ngOnInit() {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (currentUser) {
      this.router.navigate(['/home']);
    }
  }

  onSubmit() {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    // console.log(this.loginForm.value.email, this.loginForm.value.password);
    this.usersService.login(this.loginForm.value.email, this.loginForm.value.password)
      .subscribe(
        data => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
          if (data.code === 1) {
            this.usersService.authenticatedUser = data;
            localStorage.setItem('currentUser', JSON.stringify(data));
            this.router.navigate(['/home']);
          } else {
            // credenciales invalidas
            this.error = "Usuario o Contraseña incorrecto(s)";
          }
        },
        err => {
          this.error = err;
        }
      )
  }
}
