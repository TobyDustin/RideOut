import {Component, OnInit} from '@angular/core';
import {RideOut} from "../../models/rideout";
import {RideOutService} from "../../services/rideout/ride-out.service";
import {Checkpoint} from "../../models/checkpoint";

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

  getSWCoord(rideOut: RideOut) {
    const modifier = 1.8;

    let lat: number;
    let lon: number;

    rideOut.checkpoints.forEach((checkpoint: Checkpoint) => {
      if (lat == null) {
        lat = checkpoint.lat;
      } else {
        if (checkpoint.lat < lat) {
          lat = checkpoint.lat;
        }
      }
      if (lon == null) {
        lon = checkpoint.lon;
      } else {
        if (checkpoint.lon < lon) {
          lon = checkpoint.lon;
        }
      }
    });

    // Creates a margin for boundary
    lat -= 0.0054165 * modifier;
    lon -= 0.1484156 * modifier;

    console.log(`${lat},${lon}`);
    return `${lat},${lon}`;
  }

  getNECoord(rideOut: RideOut) {
    const modifier = 1.8;

    let lat: number;
    let lon: number;

    rideOut.checkpoints.forEach((checkpoint: Checkpoint) => {
      if (lat == null) {
        lat = checkpoint.lat;
      } else {
        if (checkpoint.lat > lat) {
          lat = checkpoint.lat;
        }
      }
      if (lon == null) {
        lon = checkpoint.lon;
      } else {
        if (checkpoint.lon > lon) {
          lon = checkpoint.lon;
        }
      }
    });

    // Creates a margin for boundary
    lat += 0.0054165 * modifier;
    lon += 0.1484156 * modifier;

    return `${lat},${lon}`;
  }

}
