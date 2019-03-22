import { Component, OnInit } from '@angular/core';
import {User} from "../../models/user";
import {UserService} from "../../services/user/user.service";
import {MatDialog} from "@angular/material";
import {AddRiderInfoComponent} from "./add-rider-info/add-rider-info.component";

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
    this.service.getUser().subscribe((user: User) => {
      this.user = user;
    });
  }

  profileComplete(): boolean {
    return (
      this.user.riderInformation.isInsured != null &&
      this.user.riderInformation.license != null &&
      this.user.riderInformation.emergencyContactNumber != null
    );
  }

  openDialog() {
    let dialogRef = this.dialog.open(AddRiderInfoComponent);
  }

}
