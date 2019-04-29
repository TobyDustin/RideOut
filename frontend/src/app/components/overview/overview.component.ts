import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RideOut} from "../../models/rideout";
import {RideOutService} from "../../services/rideout/ride-out.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private service: RideOutService,
    private sanitizer: DomSanitizer
  ) { }

  public rideOut: RideOut;

  ngOnInit() {
    let rideOutId = "";
    this.route.paramMap.subscribe((params) => {
      rideOutId = params.get('rideout');
    });
    this.getRideOut(rideOutId);
  }

  getRideOut(rideOutId: string) {
    this.service.getRideOut(rideOutId).subscribe((rideOut) => {
      this.rideOut = rideOut;
    })
  }

  getRoute() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(`https://www.google.com/maps/d/embed?mid=${this.rideOut.route || ""}`);
  }

}
