import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../service/user.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  loading = false;

  errorMessage = '';
  roles: string[] = [];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.pattern("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,10}$")]),
      password: new FormControl(null, [Validators.required, Validators.minLength(8), Validators.maxLength(20)])
    })
  }

  onSubmit() {
    this.userService.logIn(this.form.value)
      .subscribe(() => {
          this.router.navigate(['user/home']);
        },
        error => {
          this.errorMessage = this.handleError(error);
        });
  }

  private handleError(error: HttpErrorResponse): string {
    if (error.error instanceof ErrorEvent)
      return `No internet connection`;
    return error.message;
  }
}

