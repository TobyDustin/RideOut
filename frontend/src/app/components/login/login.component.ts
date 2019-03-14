import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth/auth.service";
import {MatSnackBar} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public username: string = "";
  public password: string = "";

  constructor(
    private router: Router,
    private service: AuthService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() { }

  authenticateUser() {
    this.service.login(this.username, this.password)
      .subscribe(
        () => {
          this.router.navigate(['/dashboard']);
        },
        (err: HttpErrorResponse) => {
          if (err.status === 403) {
            this.snackBar.open("Incorrect login!", "Dismiss")
          } else {
            this.snackBar.open(`An error occurred! ${err.statusText}`, "Dismiss")
          }
        }
      );
  }

  buttonDisabled() {
    return !(this.username.length > 0 && this.password.length > 0)
  }

}
