import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {MatDialog} from "@angular/material";
import {AddRiderInfoComponent} from "./add-rider-info/add-rider-info.component";
import {RiderInformation} from "../../models/rider";
import {Observable} from "rxjs";
import {User} from "../../models/user";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public user: User;

  constructor(
    private service: UserService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.getUser();
  }

  getUser() {
    this.service.getUser().subscribe((user) => {
      this.user = user;
    });
  }

  profileComplete(): boolean {
    return (
      this.user.riderInformation.license != null &&
      this.user.riderInformation.emergencyContactNumber != null
    );
  }

  openDialog() {
    let dialogRef = this.dialog.open(AddRiderInfoComponent, {
      data: this.user.riderInformation
    });
    dialogRef.afterClosed().subscribe(() => {
      this.getUser();
    });
  }

}
