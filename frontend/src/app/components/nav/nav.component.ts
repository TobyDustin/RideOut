import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {UserService} from "../../services/user/user.service";
import {Router} from "@angular/router";
import * as jwt_decode from "jwt-decode"

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  constructor(
    private breakpointObserver: BreakpointObserver,
    private router: Router,
    private service: UserService
  ) {}

  logout() {
    this.service.logout();
    this.router.navigate(['login']);
  }

  isLoggedIn() {
    return (localStorage.getItem('access_token') !== null)
  }

  getUsername() {
    return jwt_decode(localStorage.getItem('access_token')).username
  }

}
