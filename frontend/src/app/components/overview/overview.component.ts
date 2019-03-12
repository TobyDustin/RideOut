import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RideOut} from "../../models/rideout";
import {RideOutService} from "../../services/rideout/ride-out.service";
import * as mapboxgl from 'mapbox-gl';
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  constructor(private route: ActivatedRoute, private service: RideOutService) { }

  private rideOut = new RideOut();

  ngOnInit() {
    let rideOutId = "";
    this.route.paramMap.subscribe((params) => {
      rideOutId = params.get('rideout');
    });
    this.getRideOut(rideOutId);
    this.loadMap();
  }

  getRideOut(rideOutId: String) {
    this.service.getRideOut(rideOutId).subscribe((rideOut) => {
      this.rideOut = rideOut;
    })
  }

  loadMap() {
    (mapboxgl as any).accessToken = environment.mapbox;
    const map = new mapboxgl.Map({
      container: 'map',
      style: 'mapbox://styles/mapbox/streets-v9',
      zoom: 5,
      center: [-78.880453, 42.897852]
    });
  }

}
