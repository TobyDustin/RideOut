import { Component, OnInit } from '@angular/core';
import {User} from "../../models/user";
import {UserService} from "../../services/user/user.service";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user: User;
  passwordVerify;

  constructor(
    private router: Router,
    private snackBar: MatSnackBar,
    private service: UserService,
    private auth: AuthService
  ) { }

  ngOnInit() {
    this.user = new User();
  }

  buttonDisabled() {
    return (
      this.user.username == null
      || this.user.password == null
      || this.passwordVerify == null
      || this.user.firstName == null
      || this.user.lastName == null
      || this.user.contactNumber == null
      || this.user.dateOfBirth == null
    )
  }

  registerUser() {
    this.service.register(this.user)
      .subscribe(
        (res) => {
          if(res) {
            // If registration successful, go to dashboard
            this.auth.login(this.user.username, this.user.password)
              .subscribe(
                () => { this.router.navigate(['dashboard']) },
                (err: HttpErrorResponse) => {
                  this.snackBar.open(err.message, "Dismiss")
                }
              )
          }
        },
        (err: HttpErrorResponse) => {
          this.snackBar.open(err.message, "Dismiss")
        }
      );
  }

}
