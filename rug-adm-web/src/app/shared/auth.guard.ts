import { CanActivate, CanActivateChild, Router, ActivatedRouteSnapshot } from "@angular/router";
import { Injectable } from "@angular/core";
import { UsersService } from "./users.service";

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor(private router: Router,
    private usersService: UsersService) {}

  canActivate(route: ActivatedRouteSnapshot) {
    const currentUser = this.usersService.authenticatedUser;
    if (currentUser) {
      const roles = route.data.roles;
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot) {
    const currentUser = this.usersService.authenticatedUser;
    if (currentUser) {
      const roles = childRoute.data.roles;
      if (typeof roles == 'undefined' || roles.indexOf(currentUser.rol) > -1) {
        return true;
      }
      this.router.navigate(['/home']);
      return false;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
