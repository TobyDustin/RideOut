import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RideOut} from "../../models/rideout";
import {RideOutService} from "../../services/rideout/ride-out.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MatDialog, MatSnackBar} from "@angular/material";
import {JoinOverviewComponent} from "./join-overview/join-overview.component";
import {UserService} from "../../services/user/user.service";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  constructor(
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private auth: AuthService,
    private service: RideOutService,
    private userService: UserService,
    private sanitizer: DomSanitizer,
    private snackBar: MatSnackBar
  ) { }

  public rideOut: RideOut;
  public userHasJoined: boolean;

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
      this.userHasJoined = this.checkUserJoined(rideOut);
    })
  }

  private checkUserJoined(rideOut: RideOut) : boolean {
    let riderHasJoined = false;
    rideOut.riders.forEach((rider) => {
      if (rider.id == this.auth.getId()) {
        riderHasJoined = true;
      }
    });
    return riderHasJoined;
  }

  getRoute() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(`https://www.google.com/maps/d/embed?mid=${this.rideOut.route || ""}`);
  }

  openDialog() {
    let dialogRef = this.dialog.open(JoinOverviewComponent, {
      data: { rideOut: this.rideOut }
    });
  }

  leaveRideOut() {
    this.userService.leaveRideOut(this.rideOut.id).subscribe(
      () => {
        this.snackBar.open("Left RideOut!")._dismissAfter(5000);
      },
      () => {
        this.snackBar.open("An Error Occurred!")._dismissAfter(5000);
      }
    );
  }

}
