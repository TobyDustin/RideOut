import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map} from "rxjs/operators";
import {User} from "../../models/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

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

  register(user: User) {
    return this.http.post(`${environment.api}/rider`,
      user,
      {
        responseType: 'text'
      })
  }
}
