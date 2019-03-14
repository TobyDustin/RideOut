import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
    if (this.isLoggedIn()) {
      this.router.parseUrl("/dashboard");
    }
  }

  isLoggedIn() {
    return (localStorage.getItem('access_token') !== null)
  }

}
