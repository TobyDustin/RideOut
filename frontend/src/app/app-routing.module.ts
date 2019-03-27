import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {UserGuard} from "./guard/user.guard";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {LoginComponent} from "./components/login/login.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {LandingComponent} from "./components/landing/landing.component";
import {OverviewComponent} from "./components/overview/overview.component";
import {VehicleComponent} from "./components/vehicle/vehicle.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {AdminGuard} from "./guard/admin.guard";
import {AdminRideoutsComponent} from "./components/admin/admin-rideouts/admin-rideouts.component";

const routes: Routes = [
  {
    path: '',
    component: LandingComponent},
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [UserGuard]
  },
  {
    path: 'rideout/:rideout',
    component: OverviewComponent,
    canActivate: [UserGuard]
  },
  {
    path: 'vehicle',
    component: VehicleComponent,
    canActivate: [UserGuard]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [UserGuard]
  },
  {
    path: 'admin',
    children: [
      {
        path: '',
        component: AdminRideoutsComponent
      }
    ],
    canActivate: [AdminGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegistrationComponent
  }
];

@NgModule({
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
