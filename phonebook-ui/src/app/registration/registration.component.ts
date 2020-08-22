import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {ConfirmedValidator} from "./confirmed.validator";
import {UserService} from "../service/user.service";
import {HttpErrorResponse} from "@angular/common/http";
import {throwError} from "rxjs";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent implements OnInit {

  title = 'Sign up';
  angForm: FormGroup;
  loading: boolean;
  error: string;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService) {

    this.createForm();
  }

  createForm() {
    this.angForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,10}$")]],
      password: ['', [Validators.required, Validators.minLength(8)],
        [Validators.required, Validators.maxLength(20)]],
      confirm_password: ['', [Validators.required]]
    }, {
      validators: ConfirmedValidator('password', 'confirm_password')
    });
  }

  ngOnInit(): void {
    this.userService.getUser().subscribe(value => console.log(value), error1 => console.log(error1))
    this.userService.getTest().subscribe(value => console.log(value), error1 => console.log(error1))

  }

  onSubmit() {
    // @ts-ignore
    this.loading = true;
    this.userService.newUserRegistration(this.angForm.value)
      .subscribe(
        data => {
          this.router.navigate(['user/activate-email']);
        },
        error => {
          // @ts-ignore
          this.error = handleError(error);
          this.loading = false;
        }
      )

    function handleError(error: HttpErrorResponse) {
      if (error.error instanceof ErrorEvent) {
        // network error
        console.error(`No internet connection`);
      } else {
        // the response may contain hints of what went wrong
        console.error(`Error code: ${error.status}`);
      }
      // user facing error message
      return throwError(`Something bad happened; please try again later.`);
    }
  }

}
