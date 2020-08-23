import {Injectable} from '@angular/core'
import {User} from "./interface";
import {HttpClient} from '@angular/common/http'
import {Observable} from "rxjs";
import {Router} from "@angular/router";

@Injectable()
export class UserService {

  private readonly forgotPasswordPath = '/api/user/password/recovery/';
  private readonly resetPasswordPath = '/api/user/password/';
  private readonly userPath = '/api/user/';
  private readonly activationPath = '/api/user/activation/';
  private readonly login = '/api/user/login';
  private readonly testEndPoint = '/api/test/test-1';
  private readonly getUserEndPoint = '/api/get-user';
  public user: Observable<User>;

  constructor(private http: HttpClient, private router: Router) {
  }

  newUserRegistration(user: User) {
    return this.http.post<User>(this.userPath, user);
  }

  sendRequestToConfirmRegistration(token: string) {
    return this.http.get(`${this.activationPath}${token}`)
  }

  forgotPassword(user: User) {
    return this.http.post<User>(this.forgotPasswordPath, user);
  }

  resetPassword(user: User, token: string) {
    return this.http.put<User>(this.resetPasswordPath, {
      password: user.password,
      token: token
    });
  }

  logIn(user: User): Observable<any> {
    return this.http.post<any>(this.login, user)
  }

  getUser(): Observable<User> {
    if (!this.user)
      this.reloadUser();
    return this.user
  }

  reloadUser() {
    this.user = this.http.get<User>(this.getUserEndPoint);
  }

  getTest() {
    return this.http.get(this.testEndPoint);
  }
}
