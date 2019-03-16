import { Component, OnInit } from '@angular/core';
import {Vehicle} from "../../models/vehicle";
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-vehicle',
  templateUrl: './vehicle.component.html',
  styleUrls: ['./vehicle.component.css']
})
export class VehicleComponent implements OnInit {

  public vehicles: Vehicle[] = Array<Vehicle>();
  public displayedColumns = ["make", "model", "power", "registration", "isChecked"];

  constructor(private service: UserService) { }

  ngOnInit() {
    this.getVehicles()
  }

  getVehicles() {
    this.service.getVehicles().subscribe((vehicles: Vehicle[]) => {
      this.vehicles = vehicles;
    })
  }

}
