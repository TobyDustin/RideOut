import {Component, OnInit} from '@angular/core';
import {RideOut} from "../../models/rideout";
import {RideOutService} from "../../services/rideout/ride-out.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  rideOuts: RideOut[];

  constructor(private service: RideOutService) {}

  ngOnInit(): void {
    this.getRideOuts()
  }

  getRideOuts() {
    this.service.getAllRideOuts()
      .subscribe((rideOuts) => {
        this.rideOuts = rideOuts;
      })
  }
}
