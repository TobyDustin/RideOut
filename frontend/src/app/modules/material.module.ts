import { NgModule } from "@angular/core";
import {
  MatCardModule,
  MatDividerModule,
  MatButtonModule,
  MatIconModule,
  MatToolbarModule,
  MatSidenavModule,
  MatListModule,
  MatGridListModule,
  MatMenuModule
} from "@angular/material";

@NgModule({
  exports: [
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatGridListModule,
    MatMenuModule
  ]
})
export class MaterialModule { }
