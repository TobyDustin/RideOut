import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map} from "rxjs/operators";
import {User} from "../../models/user";
import {Observable} from "rxjs";
import {AuthService} from "../auth/auth.service";
import {Vehicle} from "../../models/vehicle";
import {RiderInformation} from "../../models/rider";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  register(user: User) {
    return this.http.post(`${environment.api}/user`,
      user,
      {
        responseType: 'text'
      })
  }

  getUser() {
    const id = this.auth.getId();
    return this.http.get(`${environment.api}/user/${id}`);
  }

  getVehicles() {
    const id = this.auth.getId();
    return this.http.get(`${environment.api}/user/${id}/vehicle`);
  }

  addVehicle(vehicle: Vehicle) {
    const id = this.auth.getId();
    return this.http.post(`${environment.api}/user/${id}/vehicle`, vehicle);
  }

  updateRiderInfo(riderInfo: RiderInformation) {
    const id = this.auth.getId();
    // TODO modify once route has been confirmed
    return this.http.put(`${environment.api}/user/${id}/riderinfo`, riderInfo)
  }
}
