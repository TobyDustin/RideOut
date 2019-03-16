import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map} from "rxjs/operators";
import {User} from "../../models/user";
import {Observable} from "rxjs";
import {AuthService} from "../auth/auth.service";

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

  getVehicles() {
    const id = this.auth.getId();
    return this.http.get(`${environment.api}/user/${id}/vehicle`);
  }
}
