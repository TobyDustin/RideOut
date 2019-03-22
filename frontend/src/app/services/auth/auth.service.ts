import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {map} from "rxjs/operators";
import * as jwt_decode from "jwt-decode"

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string) : Observable<boolean> {

    return this.http.post<{token: string}>(`${environment.api}/authenticate`,
      {
        username: username,
        password: password
      })
      .pipe(
        map(
          (res) => {
            localStorage.setItem('access_token', res.token);
            return true;
          }
        )
      )
  }

  logout() {
    localStorage.removeItem('access_token');
  }

  isLoggedIn() {
    return (this.getToken() != null);
  }

  getUsername() {
    return jwt_decode(this.getToken()).username
  }

  getToken() {
      return localStorage.getItem('access_token');
  }

}
