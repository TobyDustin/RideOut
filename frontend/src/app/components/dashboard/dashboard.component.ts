import {Component, OnInit} from '@angular/core';
import {RideOut} from "../../models/rideout";
import {RideOutService} from "../../services/rideout/ride-out.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public rideOuts: RideOut[];
  public search: String = "";
  public selectedFilters = [];
  public filters = [
    {
      name: 'Attending Only',
      value: 'attending'
    },
    {
      name: 'Available Only',
      value: 'available'
    }
  ];
  public selectedTypes = [];
  public types = [
    {
      name: 'Ride Outs',
      value: 'ride'
    },
    {
      name: 'Stay Outs',
      value: 'stay'
    },
    {
      name: 'Tour Outs',
      value: 'tour'
    }
  ];

  constructor(private service: RideOutService) {}

  ngOnInit(): void {
    this.getRideOuts();
  }

  getRideOuts() {
    this.service.getAllRideOuts()
      .subscribe((rideOuts) => {
        this.rideOuts = rideOuts;
      })
  }

  searchRideOuts() {
    this.service.searchRideOuts(this.search, this.selectedFilters, this.selectedTypes)
      .subscribe((rideOuts) => {
        this.rideOuts = rideOuts;
      })
  }
}
