import {Component, OnInit} from '@angular/core';
import {Vehicle} from "../../../models/vehicle";
import {UserService} from "../../../services/user/user.service";
import {MatDialogClose, MatDialogRef, MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-add-vehicle',
  templateUrl: './add-vehicle.component.html',
  styleUrls: ['./add-vehicle.component.css']
})
export class AddVehicleComponent implements OnInit {

  public vehicle: Vehicle;

  constructor(
    private ref: MatDialogRef<any>,
    private service: UserService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.vehicle = new Vehicle();
  }

  addVehicle() {
    this.service.addVehicle(this.vehicle).subscribe((vehicle) => {
      if (vehicle) {
        this.snackBar.open("Vehicle added!")._dismissAfter(5000);
        this.ref.close();
      } else {
        this.snackBar.open("An error occurred!")._dismissAfter(5000);
      }
    })
  }

  close() {
    this.ref.close();
  }

}
