import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from "@angular/material";
import {UserService} from "../../../services/user/user.service";
import {RideOutService} from "../../../services/rideout/ride-out.service";
import {Vehicle} from "../../../models/vehicle";
import {Observable} from "rxjs";

@Component({
  selector: 'app-join-overview',
  templateUrl: './join-overview.component.html',
  styleUrls: ['./join-overview.component.css']
})
export class JoinOverviewComponent implements OnInit {

  constructor(
    private ref: MatDialogRef<any>,
    private userService: UserService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  public vehicles: Vehicle[];
  public selectedVehicle: Vehicle;

  ngOnInit() {
    this.getUserVehicles();
  }

  getUserVehicles() {
    this.userService.getVehicles().subscribe(
      (vehicles) => {
        this.vehicles = vehicles;
      }
    )
  }

  joinRideOut() {
    this.userService.joinRideOut(this.data.rideOut.id, this.selectedVehicle.id).subscribe(
      () => {
        this.snackBar.open("Joined RideOut!")._dismissAfter(5000);
        this.close();
      },
      (err) => {
        this.snackBar.open("An Error Occurred!")._dismissAfter(5000);
      }
    );
  }

  close() {
    this.ref.close();
  }

}
