import { Component, OnInit } from '@angular/core';
import {Vehicle} from "../../models/vehicle";
import {UserService} from "../../services/user/user.service";
import {AddVehicleComponent} from "./add-vehicle/add-vehicle.component";
import {MatDialog} from "@angular/material";
import {Observable} from "rxjs";
import {ContentObserver} from "@angular/cdk/observers";

@Component({
  selector: 'app-vehicle',
  templateUrl: './vehicle.component.html',
  styleUrls: ['./vehicle.component.css']
})
export class VehicleComponent implements OnInit {

  public vehicles: Observable<Vehicle[]>;
  public displayedColumns = ["make", "model", "power", "registration", "checked"];

  constructor(private service: UserService, private dialog: MatDialog) { }

  ngOnInit() {
    this.getVehicles()
  }

  getVehicles() {
    this.vehicles = this.service.getVehicles()
  }

  openDialog() {
    let dialogRef = this.dialog.open(AddVehicleComponent);
    dialogRef.afterClosed().subscribe(() => {
      this.getVehicles();
    })
  }

}
