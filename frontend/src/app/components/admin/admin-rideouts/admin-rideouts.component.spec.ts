import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRideoutsComponent } from './admin-rideouts.component';

describe('AdminRideoutsComponent', () => {
  let component: AdminRideoutsComponent;
  let fixture: ComponentFixture<AdminRideoutsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminRideoutsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminRideoutsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
