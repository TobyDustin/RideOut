import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
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

  public searchRideOuts(search: String, filters: Array<string>, types: Array<string>) : Observable<RideOut[]> {
    let params = new HttpParams();
    filters.forEach((filter) => {
      params = params.append(filter, 'true');
    });
    types.forEach((type) => {
      params = params.append('type', type);
    });
    console.log(params);
    if (search == "") {
      return this.http.get<RideOut[]>(`${environment.api}/rideout`, {params});
    } else {
      return this.http.get<RideOut[]>(`${environment.api}/rideout/s/${search}`, {params});
    }
  }
}
