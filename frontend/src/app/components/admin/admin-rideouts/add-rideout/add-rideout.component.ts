import { Component, OnInit } from '@angular/core';
import {RideOut} from "../../../../models/rideout";
import {Observable} from "rxjs";
import {User} from "../../../../models/user";
import {MatDialogRef, MatSnackBar} from "@angular/material";
import {RideOutService} from "../../../../services/rideout/ride-out.service";
import {Checkpoint} from "../../../../models/checkpoint";
import {Booking} from "../../../../models/booking";
import {HttpErrorResponse} from "@angular/common/http";
import {UserService} from "../../../../services/user/user.service";

@Component({
  selector: 'app-add-rideout',
  templateUrl: './add-rideout.component.html',
  styleUrls: ['./add-rideout.component.css']
})
export class AddRideoutComponent implements OnInit {

  constructor(
    private ref: MatDialogRef<any>,
    private snackBar: MatSnackBar,
    private rideOutService: RideOutService,
    private userService: UserService
  ) { }

  public rideOut = new RideOut();
  public leadRiders: User[];
  public types = [
    'Ride',
    'Stay',
    'Tour'
  ];
  public checkpoint = new Checkpoint();
  public restaurant = new Booking();
  public accommodation = new Booking();
  public travel = new Booking();

  ngOnInit() {
    this.leadRiders = this.loadLeadRiders();
  }

  loadLeadRiders() {
    const leadRiders: User[] = [];
    this.userService.getAllUsers().subscribe(
      (users) => {
        users.forEach((user) => {
          if (user.role === 'staff' && user.riderInformation !== undefined) {
            leadRiders.push(user);
          }
        });
      }
    );
    return leadRiders;
  }

  addCheckpoint() {
    if (this.rideOut.checkpoints !== undefined) {
      this.rideOut.checkpoints.push(this.checkpoint);
      this.checkpoint = new Checkpoint();
    } else {
      this.rideOut.checkpoints = [];
      this.addCheckpoint();
    }
  }

  addRestaurant() {
    if (this.rideOut.restaurantBookings !== undefined) {
      this.rideOut.restaurantBookings.push(this.restaurant);
      this.restaurant = new Booking();
    } else {
      this.rideOut.restaurantBookings = [];
      this.addRestaurant();
    }
  }

  addAccommodation() {
    if (this.rideOut.accommodationBookings !== undefined) {
      this.rideOut.accommodationBookings.push(this.accommodation);
      this.accommodation = new Booking();
    } else {
      this.rideOut.accommodationBookings = [];
      this.addAccommodation();
    }
  }

  addTravel() {
    if (this.rideOut.travelBookings !== undefined) {
      this.rideOut.travelBookings.push(this.travel);
      this.travel = new Booking();
    } else {
      this.rideOut.travelBookings = [];
      this.addTravel();
    }
  }

  createRideOut() {
    this.rideOutService.createRideOut(this.rideOut).subscribe(
      () => {
        this.snackBar.open('RideOut Created!', 'Dismiss!');
      },
      (err: HttpErrorResponse) => {
        this.snackBar.open(`Error Creating RideOut! ${err.message}`, 'Dismiss!');
      }
    );
  }

  close() {
    this.ref.close();
  }

}
