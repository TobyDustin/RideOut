import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {MatDialog} from "@angular/material";
import {AddRiderInfoComponent} from "./add-rider-info/add-rider-info.component";
import {User} from "../../models/user";
import {isUndefined} from "util";
import {RiderInformation} from "../../models/rider";

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
    if (!isUndefined(this.user.riderInformation)) {
      return (
        !isUndefined(this.user.riderInformation.license) &&
        !isUndefined(this.user.riderInformation.emergencyContactNumber)
      );
    } else {
      return false;
    }
  }

  public openDialog() {
    let dialogRef = this.dialog.open(AddRiderInfoComponent, {
      data: this.user.riderInformation || new RiderInformation()
    });
    dialogRef.afterClosed().subscribe(() => {
      this.getUser();
    });
  }

}
