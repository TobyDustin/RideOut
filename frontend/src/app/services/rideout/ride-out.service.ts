import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {RideOut} from "../../models/rideout";

@Injectable({
  providedIn: 'root'
})
export class RideOutService {

  constructor(private http: HttpClient) { }

  public getAllRideOuts() : Observable<RideOut[]> {
    return this.http.get<RideOut[]>(`${environment.api}/rideout`);
  }

  public getRideOut(rideOut: String) : Observable<RideOut> {
    return this.http.get<RideOut>(`${environment.api}/rideout/${rideOut}`);
  }

  public searchRideOuts(search: String) : Observable<RideOut[]> {
    return this.http.get<RideOut[]>(`${environment.api}/rideout/s/${search}`);
  }
}
