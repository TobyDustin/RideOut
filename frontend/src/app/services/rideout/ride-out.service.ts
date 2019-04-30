import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {RideOut} from "../../models/rideout";
import {AuthService} from "../auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class RideOutService {

  constructor(
    private http: HttpClient,
    private auth: AuthService
  ) { }

  public getAllRideOuts() : Observable<RideOut[]> {
    return this.http.get<RideOut[]>(`${environment.api}/rideout`);
  }

  public getRideOut(rideOut: string) : Observable<RideOut> {
    return this.http.get<RideOut>(`${environment.api}/rideout/${rideOut}`);
  }

  public searchRideOuts(search: string, filters: Array<string>, types: Array<string>) : Observable<RideOut[]> {
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

  public createRideOut(rideOut: RideOut) {
    return this.http.post<RideOut>(`${environment.api}/rideout`, rideOut);
  }

}
