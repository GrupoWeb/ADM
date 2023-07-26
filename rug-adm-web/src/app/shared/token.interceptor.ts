import { Injectable, Injector } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from "@angular/common/http";
import { UsersService } from "./users.service";
import { Observable } from "rxjs/Observable";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private injector: Injector) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const usersService = this.injector.get(UsersService);

    if (usersService.authenticatedUser != null) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${usersService.authenticatedUser.token}`
        }
      });
    }
    return next.handle(request);
  }

}
