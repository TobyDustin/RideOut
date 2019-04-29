import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user/user.service";
import {Observable} from "rxjs";
import {User} from "../../../models/user";

@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {

  constructor(
    private service: UserService
  ) { }

  public users: Observable<User[]>;
  public displayedColumns = [
    "last_name",
    "first_name",
    "username",
    "role"
  ];

  ngOnInit() {
    this.loadUsers();
  }

  private loadUsers() {
    this.users = this.service.getAllUsers();
  }

}
