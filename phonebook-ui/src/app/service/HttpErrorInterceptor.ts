import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from "rxjs/operators";
import {Router} from "@angular/router";
import {Error} from "./error";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router) {
  }


  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          let errorObj = new Error();
          if (error.error instanceof ErrorEvent) {
            errorObj.message = `${error.error.message}`
          } else {
            errorObj.message = `${error.error.message}`
            errorObj.errorCode = +`${error.status}`
          }
          if (error.status === 401 && error.error.message !== 'Username or password is incorrect!')
            this.router.navigate(['user/login']);
          return throwError(errorObj);
        })
      )
  }
}
