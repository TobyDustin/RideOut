import { Component, OnInit } from '@angular/core';
import {RideOut} from "../../../models/rideout";
import {RideOutService} from "../../../services/rideout/ride-out.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-admin-rideouts',
  templateUrl: './admin-rideouts.component.html',
  styleUrls: ['./admin-rideouts.component.css']
})
export class AdminRideoutsComponent implements OnInit {

  constructor(
    private service: RideOutService
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

}
