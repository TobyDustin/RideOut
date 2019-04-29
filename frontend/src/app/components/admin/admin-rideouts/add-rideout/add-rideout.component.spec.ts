import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRideoutComponent } from './add-rideout.component';

describe('AddRideoutComponent', () => {
  let component: AddRideoutComponent;
  let fixture: ComponentFixture<AddRideoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRideoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRideoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
