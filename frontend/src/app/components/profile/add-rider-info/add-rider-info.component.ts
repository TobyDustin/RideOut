import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from "@angular/material";
import {User} from "../../../models/user";
import {UserService} from "../../../services/user/user.service";
import {RiderInformation} from "../../../models/rider";

@Component({
  selector: 'app-add-rider-info',
  templateUrl: './add-rider-info.component.html',
  styleUrls: ['./add-rider-info.component.css']
})
export class AddRiderInfoComponent implements OnInit {

  public licenseCategories = [
    "A",
    "A1",
    "A2",
  ];

  constructor(
    private ref: MatDialogRef<any>,
    private snackBar: MatSnackBar,
    private service: UserService,
    @Inject(MAT_DIALOG_DATA) public riderInfo
  ) { }

  ngOnInit() {
    this.initializeMissingValues();
  }

  initializeMissingValues() {
    if (this.riderInfo.license == null) {
      this.riderInfo.license = "";
    }
    if (this.riderInfo.emergencyContactNumber == null) {
      this.riderInfo.emergencyContactNumber = "";
    }
  }

  updateRiderInfo() {
    this.service.updateRiderInfo(this.riderInfo).subscribe(
      () => {
        this.snackBar.open("Rider information updated!")._dismissAfter(5000);
        this.ref.close();
      },
      (err: Error) => {
        this.snackBar.open("An error occurred!")._dismissAfter(5000);
      }
    );
  }

  close() {
    this.ref.close();
  }

}
