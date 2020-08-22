import {Injectable} from '@angular/core'
import {User} from "./interface";
import {HttpClient, HttpHeaders} from '@angular/common/http'
import {Observable} from "rxjs";

@Injectable()
export class UserService {

  private readonly forgotPasswordPath = '/api/user/password/recovery/';
  private readonly resetPasswordPath = '/api/user/password/';
  private readonly userPath = '/api/user/';
  private readonly activationPath = '/api/user/activation/';
  private readonly login = '/api/user/login';
  private readonly testEndPoint = '/api/test/test-1';
  private readonly getUserEndPoint = '/api/get-user';

  constructor(private http: HttpClient) {
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


  private headers: HttpHeaders;

  logIn(user: User): Observable<any> {
    this.headers = new HttpHeaders({
      "Content-Type": "application/json",
      "Access-Control-Allow-Credentials": "true"
    });

    return this.http.post<any>(this.login, user,
      {headers: this.headers, withCredentials: true});

  }

  getUser() {
    return this.http.get(this.getUserEndPoint);
  }

  getTest() {
    return this.http.get(this.testEndPoint);
  }
}
