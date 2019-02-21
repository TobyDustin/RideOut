import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {MatSnackBar} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";
import {first} from "rxjs/operators";
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
    private service: UserService,
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
