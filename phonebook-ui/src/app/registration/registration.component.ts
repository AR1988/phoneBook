import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {ConfirmedValidator} from "./confirmed.validator";
import {UserService} from "../service/user.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent implements OnInit {

  title = 'Sign up';
  angForm: FormGroup;
  error: string;

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService) {
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
    this.createForm();
  }

  onSubmit() {
    this.userService.newUserRegistration(this.angForm.value)
      .subscribe(
        data => {
          this.router.navigate(['user/activate-email']);
        },
        error => {
          this.error = this.handleError(error);
        });
  }

  private handleError(error: HttpErrorResponse): string {
    if (error.error instanceof ErrorEvent)
      return `No internet connection`;
    return error.message;
  }

}
