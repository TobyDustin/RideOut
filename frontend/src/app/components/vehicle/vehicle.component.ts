import { Component, OnInit } from '@angular/core';
import {Vehicle} from "../../models/vehicle";
import {UserService} from "../../services/user/user.service";
import {AddVehicleComponent} from "./add-vehicle/add-vehicle.component";
import {MatDialog} from "@angular/material";
import {Observable} from "rxjs";

@Component({
  selector: 'app-vehicle',
  templateUrl: './vehicle.component.html',
  styleUrls: ['./vehicle.component.css']
})
export class VehicleComponent implements OnInit {

  public vehicles: Observable<Vehicle> = new Observable<Vehicle>();
  public displayedColumns = ["make", "model", "power", "registration", "isChecked"];

  constructor(private service: UserService, private dialog: MatDialog) { }

  ngOnInit() {
    this.getVehicles()
  }

  getVehicles() {
    this.service.getVehicles().subscribe((vehicles: Observable<Vehicle>) => {
      this.vehicles = vehicles;
    })
  }

  openDialog() {
    let dialogRef = this.dialog.open(AddVehicleComponent);
  }

}
