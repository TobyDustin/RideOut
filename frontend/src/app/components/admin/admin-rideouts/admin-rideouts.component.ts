import { Component, OnInit } from '@angular/core';
import {RideOut} from "../../../models/rideout";
import {RideOutService} from "../../../services/rideout/ride-out.service";
import {Observable} from "rxjs";
import {MatDialog} from "@angular/material";
import {AddRideoutComponent} from "./add-rideout/add-rideout.component";

@Component({
  selector: 'app-admin-rideouts',
  templateUrl: './admin-rideouts.component.html',
  styleUrls: ['./admin-rideouts.component.css']
})
export class AdminRideoutsComponent implements OnInit {

  constructor(
    private service: RideOutService,
    private dialog: MatDialog
  ) { }

  public rideOuts: Observable<RideOut[]>;
  public displayedColumns = [
    "name",
    "type",
    "dateStart",
    "dateEnd",
    "riders",
    "checkpoints",
    "travelBookings",
    "accommodationBookings",
    "restaurant",
    "published"
  ];

  ngOnInit() {
    this.rideOuts = this.service.getAllRideOuts();
  }

  openDialog() {
    const dialogRef = this.dialog.open(AddRideoutComponent);
  }

}
