import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {User} from "../../models/user";
import {Observable} from "rxjs";
import {AuthService} from "../auth/auth.service";
import {Vehicle} from "../../models/vehicle";
import {RiderInformation} from "../../models/rider";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private auth: AuthService
  ) { }

  register(user: User) {
    return this.http.post(`${environment.api}/user`,
      user,
      {
        responseType: 'text'
      })
  }

  getVehicles(): Observable<Vehicle[]> {
    const id = this.auth.getId();
    return this.http.get<Vehicle[]>(`${environment.api}/user/${id}/vehicle`);
  }

  addVehicle(vehicle: Vehicle) {
    const id = this.auth.getId();
    return this.http.post(`${environment.api}/user/${id}/vehicle`, vehicle);
  }

  getUser() : Observable<User> {
    const id = this.auth.getId();
    return this.http.get<User>(`${environment.api}/user/${id}`);
  }

  getUserInfo() : Observable<RiderInformation> {
    const id = this.auth.getId();
    return this.http.get<RiderInformation>(`${environment.api}/user/${id}/riderinfo`);
  }

  updateRiderInfo(riderInfo: RiderInformation) {
    const id = this.auth.getId();
    return this.http.post(`${environment.api}/user/${id}/riderinfo`, riderInfo);
  }

  getAllUsers() : Observable<User[]> {
    return this.http.get<User[]>(`${environment.api}/user`);
  }

  joinRideOut(rideOutID: string, vehicleID: string) {
    const userID = this.auth.getId();
    return this.http.put(`${environment.api}/rideout/${rideOutID}/rider`,
      {
        'userId': userID,
        'vehicleId': vehicleID
      }
    );
  }

  leaveRideOut(rideOutID: string) {
    const userID = this.auth.getId();
    return this.http.delete(`${environment.api}/rideout/${rideOutID}/rider/${userID}`);
  }

}
