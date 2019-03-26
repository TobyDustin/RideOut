import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from "@angular/common/http";
import { JwtModule } from '@auth0/angular-jwt';
import { MaterialModule } from "./modules/material.module";
import { AppRoutingModule } from './app-routing.module';
import { LayoutModule } from '@angular/cdk/layout';
import { FormsModule } from "@angular/forms";
import { NavComponent } from './components/nav/nav.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { LandingComponent } from './components/landing/landing.component';
import { OverviewComponent } from './components/overview/overview.component';
import { AgmCoreModule } from '@agm/core';
import {environment} from "../environments/environment";
import { VehicleComponent } from './components/vehicle/vehicle.component';
import { AddVehicleComponent } from './components/vehicle/add-vehicle/add-vehicle.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AddRiderInfoComponent } from './components/profile/add-rider-info/add-rider-info.component';

export function tokenGetter() {
  return localStorage.getItem('access_token');
}

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    DashboardComponent,
    RegistrationComponent,
    LoginComponent,
    LandingComponent,
    OverviewComponent,
    VehicleComponent,
    AddVehicleComponent,
    OverviewComponent,
    ProfileComponent,
    AddRiderInfoComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    AppRoutingModule,
    LayoutModule,
    FormsModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        whitelistedDomains: ['localhost:8080']
      }
    })
  ],
  entryComponents: [
    AddVehicleComponent,
    AddRiderInfoComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
