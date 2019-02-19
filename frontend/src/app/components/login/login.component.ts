import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {MatSnackBar} from "@angular/material";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  private username: string = "";
  private password: string = "";

  constructor(private service: UserService, private snackBar: MatSnackBar) { }

  ngOnInit() { }

  authenticateUser() {
    this.service.login(this.username, this.password)
      .subscribe(
        (user) => {
          alert(user.username);
        },
        (err: HttpErrorResponse) => {
          this.snackBar.open(err.statusText, "Dismiss");
        }
      );
  }

  buttonDisabled() {
    return !(this.username.length > 0 && this.password.length > 0)
  }

}
