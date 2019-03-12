import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {LoginComponent} from "./components/login/login.component";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {RegistrationComponent} from "./components/registration/registration.component";
import {LandingComponent} from "./components/landing/landing.component";
import {OverviewComponent} from "./components/overview/overview.component";

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'rideout/:rideout', component: OverviewComponent }
];

@NgModule({
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
