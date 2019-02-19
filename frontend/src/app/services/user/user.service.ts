import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    return this.http.post<any>(`${environment.api}/user/login`,JSON.stringify({
      username: username,
      password: password
    })).pipe(map((user) => {
      if (user && user.token) {
        localStorage.setItem('currentUser', JSON.stringify(user));
      }

      return user;
    }))
  }
}
